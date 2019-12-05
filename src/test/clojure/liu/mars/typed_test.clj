(ns liu.mars.typed-test
  (:require [clojure.test :refer :all]
            [jaskell.handle :refer [function supplier]]
            [liu.mars.typed :refer [behavior ! same spawn]])
  (:import (liu.mars StateActor Actor)
           (java.util Map)
           (akka.actor.typed.javadsl ActorContext Behaviors)
           (akka.actor.testkit.typed.javadsl ActorTestKit)))

(deftest basic-test
  "tests for basic actor workflow"
  (let [test-kit (ActorTestKit/create)
        probe (.createTestProbe test-kit)
        actor (spawn test-kit (behavior
                                 (fn receiver [^ActorContext context ^Map message]
                                   (case (:category message)
                                     :inc
                                     (! (:sender message) (-> message :number inc))
                                     :dec
                                     (! (:sender message) (-> message :number dec))
                                     (! (:sender message) (-> message :number)))
                                   (same))))
        self (.ref probe)]
    (! actor {:category :inc :number 100 :sender self})
    (.expectMessage probe 101)
    (! actor {:category :dec :number 100 :sender self})
    (.expectMessage probe 99)
    (! actor {:category :save :number 100 :sender self})
    (.expectMessage probe 100)
    (.shutdownTestKit test-kit)))

(deftest basic-state-test
  "tests for basic actor workflow"
  (let [test-kit (ActorTestKit/create)
        probe (.createTestProbe test-kit)
        actor (spawn test-kit (behavior
                                 {:number 0}
                                 (fn receiver [^ActorContext context ^Map state ^Map message]
                                   (case (:category message)
                                     :inc
                                     (let [st (update state :number inc)]
                                       (! (:sender message) st)
                                       (behavior st receiver))
                                     :dec
                                     (let [st (update state :number dec)]
                                       (! (:sender message) st)
                                       (behavior st receiver))
                                     (let [st (assoc state :number (:number message))]
                                       (! (:sender message) st)
                                       (behavior st receiver))))))
        self (.ref probe)]
    (! actor {:category :inc :sender self})
    (.expectMessage probe {:number 1})
    (! actor {:category :dec :sender self})
    (.expectMessage probe {:number 0})
    (! actor {:category :save :number 100 :sender self})
    (.expectMessage probe {:number 100})
    (.shutdownTestKit test-kit)))