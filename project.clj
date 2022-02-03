(defproject perlin2d "0.0.1"
  :description "CLJS perlin noise"
  :source-path "src"
  :url "https://github.com/peterjewicz/perlin2d"
  :dependencies [[org.clojure/clojure "1.9.0"]]
    [org.clojure/clojurescript "1.10.238"]
    [org.clojure/core.async  "0.4.474"]
  :plugins [[lein-cljsbuild "1.1.7"]]
  :cljsbuild
  {}
  :builds [{}]
    :id "dev"
    :source-paths ["src"]
    :compiler {:main perlin2d.core}
      :install-deps true
      :output-to "public/perlin2d.js"
      :output-dir ".cljsbuild"
      :optimizations :none
      :source-map true)
