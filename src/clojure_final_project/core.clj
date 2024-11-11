(ns clojure-final-project.core
  (:require
   [clojure.data.csv :as csv]
   [clojure.java.io :as io]
   [uncomplicate.neanderthal.core :as core]
   [uncomplicate.neanderthal.native :as native]))

(defn read-csv
  "Takes the csv file path and returns this list of vectors.
   
   **Example**\n
    
   `user=>` (def foo (read-csv \"resources/in-file.csv\"))\n

   `File content:`\n
   foo,bar,baz\n
   A,1,x\n
   B,2,y\n
   C,3,z\n
   
   `#'user/foo`\n
   ([\" foo \" \" bar \" \" baz \"] [\" A \" \" 1 \" \" x \"] [\" B \" \" 2 \" \" y \"] [\" C \" \" 3 \" \" z \"])
   "
  [file-path]
  (with-open [reader (io/reader file-path)]
    (doall (csv/read-csv reader))))


(defn csv-data->maps
  "Takes a sequence of vectors and converts it to a sequence of maps where
  each value from the header row - first vector - becomes a keyword in a map.
   
   **Example** \n
   
   `user=>` (def foo (csv-data->maps ([\" foo \" \" bar \" \" baz \"] [\" A \" \" 1 \" \" x \"] [\" B \" \" 2 \" \" y \"] [\" C \" \" 3 \" \" z \"])))\n
   
   `#'user/foo`\n
   ({:foo \"A\", :bar \"1\", :baz \"x\"} {:foo \"B\", :bar \"2\", :baz \"y\"} {:foo \"C\", :bar \"3\", :baz \"z\"})
   "
  [csv-data]
  (map zipmap
       (->> (first csv-data)
            (map keyword)
            repeat)
       (rest csv-data)))


(defn split-train-test-validation
  "Splits a dataset into training, testing, and validation subsets based on specified percentages.

  Parameters:
  - `data`: A sequence of maps representing the dataset.
  - `[train-percentage test-percentage validation-percentage]`: A vector of three decimal values representing the split percentages for training, testing, and validation subsets, respectively. These percentages must add up to 1.0.

  Returns:
  A map with keys:
  - `:train` - The training dataset as a sequence of maps.
  - `:test` - The testing dataset as a sequence of maps.
  - `:validation` - The validation dataset as a sequence of maps.
  "
  [data [train-percentage test-percentage validation-percentage]]
  (if (not (= 1.0 (+ train-percentage test-percentage validation-percentage)))
    (println "Make sure percentages add up to 1")
    (let [shuffled-data (shuffle data)
          total-count (count data)
          train-count (int (* train-percentage total-count))
          test-count (int (* test-percentage total-count))
          train-data (take train-count shuffled-data)
          test-data (take test-count (drop train-count shuffled-data))
          validation-data (drop (+ train-count test-count) shuffled-data)]
      {:train train-data
       :test test-data
       :validation validation-data})))

;; TODO IMPLEMENT PREPROCESSING FUNCTION - DEAL WITH EMPTY VALUES AND INVALID DATA

;; (let [data (csv-data->maps (read-csv "resources/in-file.csv"))
      ;; coll (split-train-test-validation (csv-data->maps (read-csv "resources/in-file.csv")) [0.4 0.3 0.3])]  coll)


;; Linear Algebra refresher

;; Null vector in R^7
(def v1 (native/dv 7))
v1

;; Vector in R^4
(def v2 (native/dv 1.0 2.0 3.4 2.5))
v2

;; Vector in R^22 
(def v3 (native/dv (range 22)))
v3

;; Adding two vectors (xpy v1 v2) - vector x plus vector y
(def v4 (native/dv (range 4)))
(def v5 (core/xpy v4 v2))

;; Scalar multiplication
(core/scal 2.5 v2)

;; We can fuse scal and xpy into axpy (scalar a times vector x plus vector y)
(core/axpy! 2.5 v4 v2)
v2
;; ZASTO SE V2 PROMENIO?
;; ! u CLojure oznacava mutabilnu operaciju

;; Zero vector in the same vector space sa v2
(core/zero v2)

;; There are only two independent opeartions in R^n
;; Vector addition and scalar multiplication
;; Vector substraction is simply an addition with negative vector
;; We can do it with axpy or axpby
(core/axpby! 1 v2 0 v4)

;; Linear Combinations of Vectors
;; au + bv + cw
(let [u (native/dv 2 5 -3)
      v (native/dv 1 1 1)
      w (native/dv 2 2 2)]
  (core/axpy 2 u -3 v 1 w))

;; Column Vectors - Both row vectors and column vectors are represented in the same way
;; Dot product
;; u dot v = u1v1 + u2v2 + u3v3 +...+ unvn
(let [u (native/dv 1 -2 4)
      v (native/dv 2 2 4)]
  (core/dot u v))

;; Norm(aka length, or maginitude, L2 norm) most often refers to Euclidian distance between two vectors
;; ||u||=sqrt(u1^2 + u2^2 + ... un^2)
(core/nrm2 (native/dv 1 3 5))

;; A unit vector is a vector whos norm is 1, we can construct a unit vector u in the same direction
;; u = (1/||v||) * V
(let [v (native/dv 1 3 5)]
  (core/scal (/ 1 (core/nrm2 v)) v))
;; This is called normalizing the vector


;; Angle between vectors
;; cos(theta) = (u dot v)/(||u||*||v||)
(let [u (native/dv 1 0 0)
      v (native/dv 1 0 1)]
  (/ (core/dot u v) (* (core/nrm2 u) (core/nrm2 v))))

;; We can determine distance between points in a vector space by calculating the norm difference between their direction vectors
;; d(x, y) = ||x-y||
(let [x (native/dv 4 0 -3 5)
      y (native/dv 1 -2 3 0)]
  (core/nrm2 (core/axpy -1 x y)))

;; General Vector Spaces
;; Matrix

