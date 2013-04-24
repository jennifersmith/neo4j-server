(ns neo4j-server.core
  (:import
   [org.neo4j.graphdb.factory
    GraphDatabaseFactory
    GraphDatabaseSettings
    GraphDatabaseSettings$CacheTypeSetting]
   [org.neo4j.kernel EmbeddedGraphDatabase]
   [org.neo4j.server WrappingNeoServerBootstrapper]))


(def db (atom nil))
(def server (atom nil))

(def shutdown-database
  (fn [db]
    (if db (.shutdown db))))

(def create-database
  (fn [db path]
    (shutdown-database db)
    (new EmbeddedGraphDatabase path)))

(def stop-server
  (fn [server]
    (if server (.stop server))
    nil))

(defn start-server [database]
  (doto (new WrappingNeoServerBootstrapper database)
    (.start)))

(defn start-all [path]
  (swap! db create-database path)
  (swap! server start-server @db))

(defn stop-all []
  (swap! server stop-server)
  (swap! db shutdown-database))

;; hackety hack
(defprotocol DbContainer
  (get-db [this]))

(defn wrapping-server [database]
  (let [server (start-server (get-db database))]
    (reify
      java.io.Closeable
      (close [this] (.stop server)))))

(defn configure [builder settings]
  (reduce #(.setConfig %1 (key settings) (val settings)) builder settings))


(defn embedded-database [database-path settings]
  (let [db
        (-> (new GraphDatabaseFactory)
            (.newEmbeddedDatabaseBuilder database-path)
            (configure settings)
            (.newGraphDatabase))]
    (reify
      ;;bugger
      DbContainer
      (get-db [this] db)
      java.io.Closeable
      (close [this] (.shutdown db)))))

(defn -main [path]
  (with-open [db (embedded-database path {})
              server (wrapping-server db)]
    (println "Running... press any key to quit")
    (read)))