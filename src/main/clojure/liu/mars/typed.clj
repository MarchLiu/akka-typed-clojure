(ns liu.mars.typed
  (:import (liu.mars StateActor Actor)
           (akka.actor.typed.javadsl Behaviors)
           (akka.actor.typed Behavior)))

(defn behavior
  ([state receiver]
   (StateActor/create state receiver))
  ([receiver]
   (Actor/create receiver)))

(defn spawn
  ([system ^Behavior behavior]
   (.spawn system behavior))
  ([system ^Behavior behavior ^String name]
   (.spawn system behavior name)))

(defn same
  []
  (Behaviors/same))

(defn stopped
  []
  (Behaviors/stopped))

(defn !
  [actor message]
  (.tell actor message))

(defn tell
  "just same as ! "
  [actor message]
  (.tell actor message))