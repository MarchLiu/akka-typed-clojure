(ns liu.mars.typed.behaviors
  (:require [jaskell.duration :refer [of-seconds]]
            [jaskell.handle :refer [function]]
            [liu.mars.utils :refer [function2]])
  (:import (liu.mars.typed StateActor Actor)
           (akka.actor.typed.javadsl Behaviors ActorContext)
           (akka.actor.typed Behavior ActorSystem)
           (akka.japi.function Function2)
           (java.util.function Function)))

(defn behavior
  ([state receiver]
   (StateActor/create state receiver))
  ([receiver]
   (Actor/create receiver)))

(defn system
  ([receiver name]
   (ActorSystem/create (behavior receiver) name))
  ([state receiver name]
   (ActorSystem/create (behavior state receiver) name)))

(defn spawn
  ([context ^Behavior behavior]
   (.spawn context behavior))
  ([context ^Behavior behavior ^String name]
   (.spawn context behavior name)))

(defn spa
  [context receiver & options]
  (let [{:keys [name state]} options
        behavior (if state
                   (behavior state receiver)
                   (behavior receiver))]
    (if name
      (spawn context behavior name)
      (spawn context behavior))))

(defn setup
  ([receiver]
   (Behaviors/setup
     (function [context]
       (receiver context)))))

(defn receive
  ([on-message]
   (Behaviors/receive
     ^Function2 (function2 [context behavior]
                  (on-message context behavior))))
  ([on-message on-signal]
   (Behaviors/receive
     ^Function2 (function2 [context message]
                  (on-message context message))
     ^Function2 (function2 [context signal]
                  (on-signal context signal)))))

(defn receive-message
  [on-message]
  (Behaviors/receiveMessage
    ^Function (function [behavior]
                (on-message behavior))))

(def same
  (Behaviors/same))

(def stopped
  (Behaviors/stopped))

(def ignore
  (Behaviors/ignore))

(defn !
  [actor message]
  (.tell actor message))

(defn tell
  "just same as ! "
  [actor message]
  (.tell actor message))

(defn ask
  ([^ActorContext context actor response-class message handle]
   (ask context actor response-class message (of-seconds 3) handle))
  ([^ActorContext context actor response-class request handle timeout]
   (.ask context
         actor
         response-class
         (function [replay-to]
           (request replay-to))
         timeout
         (function2 [reply failure]
           (handle reply failure)))))