(defproject neo4j-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.neo4j/neo4j "1.9.M03"]
                 [org.neo4j.app/neo4j-server "1.9.M03"]
                 [org.neo4j.app/neo4j-server "1.9.M03" :classifier "static-web"]]
  :repositories [["neo4j-snapshot-repository"
                  { :url "http://m2.neo4j.org/content/repositories/snapshots/"
                   :snapshots true
                   :releases false}]]
  :jvm-opts ["-Xmx2g"]
  :main neo4j-server.core)
