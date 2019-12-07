(defproject liu.mars/akka-typed-clojure "0.0.2"
  :description "akka typed toolkit for clojure programmers"
  :url "https://github.com/MarchLiu/akka-clojure-typed"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/main/clojure"]
  :java-source-paths ["src/main/java"]
  :test-paths ["src/test/clojure"]
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.typesafe.akka/akka-actor-typed_2.13 "2.6.0"]
                 [liu.mars/jaskell "0.2.7"]]
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]
  :profiles {:test {:dependencies          [[com.typesafe.akka/akka-actor-testkit-typed_2.13 "2.6.0"]]
                    :plugins               [[lein-test-report-junit-xml "0.2.0"]]
                    :test-report-junit-xml {:output-dir "target/surefire-reports"}}})
