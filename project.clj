(defproject clojure-final-project "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [org.clojure/data.csv "1.1.0"]
                 [uncomplicate/neanderthal "0.50.0"]
                ;;  [org.bytedeco/mkl "2024.0-1.5.10" :classifier linux-x86_64-redist]
                ;;  [org.bytedeco/cuda "12.3-8.9-1.5.10" :classifier linux-x86_64-redist]
                 ;; On windows, replace the last two lines with:
                 [org.bytedeco/mkl "2024.0-1.5.10" :classifier windows-x86_64-redist]
                 [org.bytedeco/cuda "12.3-8.9-1.5.10" :classifier windows-x86_64-redist]]
  :jvm-opts ^:replace ["-Dclojure.compiler.direct-linking=true"] ;; We need direct linking for properly resolving types in heavy macros and avoiding reflection warnings!
  :repl-options {:init-ns clojure-final-project.core})
