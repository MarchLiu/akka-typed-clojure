(ns liu.mars.utils
  (:import (akka.japi.function Function2)))

(defmacro function2 [[x y] & body]
  `(reify Function2
     (apply [_ ~x ~y]
       ~@body)))

