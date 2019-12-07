(ns liu.mars.typed.partterns
  (:require [jaskell.duration :refer [of-seconds]]
            [jaskell.handle :refer [function]]
            [liu.mars.utils :refer [function2]])
  (:import (akka.actor.typed.javadsl AskPattern)
           (akka.actor.typed ActorSystem)))

(defn ask
  ([actor request handle ^ActorSystem system]
   (ask actor request handle system (of-seconds 3)))
  ([actor request handle ^ActorSystem system timeout]
   (-> (AskPattern/ask
         actor
         (function [replay-to]
           (request replay-to))
         timeout
         (.scheduler system))
       (.whenComplete
         (function2 [reply failure]
           (handle reply failure))))))
