(ns perlin2d.core)

(def p1
  [151,160,137,91,90,15,
   131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
   190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
   88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
   77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
   102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
   135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
   5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
   223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
   129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
   251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
   49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
   138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180])

(def generated (atom nil)) ; we record our seed map here so we don't regenerate it on every pass

(defn generate-by-seed [seed]
  (let [base (concat p1 p1)]
    (map #(+ % seed) base)))

(defn get-P [seed]
  (cond 
    (and seed (not @generated)) (reset! generated (generate-by-seed seed))
    (and (not seed) (not @generated)) (reset! generated (shuffle (concat p1 p1))))) ; just use our shuffled value prevents warp

(defn create-vector [x y]
  {:x x :y y})

(defn calc-dot-product [v1 v2]
  (+ (* (:x v1) (:x v2)) (* (:y v1) (:y v2))))

(defn get-constant-vector [v]
  (let [h (bit-and v 3)]
    (cond
      (= h 0) (create-vector 1.0 1.0)
      (= h 1) (create-vector -1.0 1.0)
      (= h 2) (create-vector -1.0 -1.0)
      (= h 3) (create-vector 1.0 -1.0))))

(defn fade [t]
  (* t t t (+ 10 (* t (- (* 6 t) 15)))))

(defn lin-interp [t a1 a2]
  (+ a1 (* t (- a2 a1))))

(defn perlin2D [initialX initialY & [seed]] 
  (get-P seed) ; either generates our seed map or ignores if already set
  (let [x (bit-and initialX 255)
        y (bit-and initialY 255)
        xf (- initialX (.floor js/Math initialX))
        yf (- initialY (.floor js/Math initialY))
        topRight (create-vector (- xf 1.0) (- yf 1.0)) ; Calc unit vectors from grid corners to point
        topLeft  (create-vector xf (- yf 1.0))
        bottomRight (create-vector (- xf 1.0) yf)
        bottomleft (create-vector xf yf)
        ; get values to create our constant vectors
        valueTopRight (nth @generated (+ (nth @generated (+ x 1)) 1 y))
        valueTopLeft (nth @generated (+ (nth @generated x) 1 y))
        valueBottomRight (nth @generated (+ (nth @generated (+ x 1)) y)) ; F(0,0) = p[1] + 0
        valueBottomLeft (nth @generated (+ (nth @generated x) y))

        ; Get out dot products
        dotTopRight (calc-dot-product topRight (get-constant-vector valueTopRight))
        dotTopLeft (calc-dot-product topLeft (get-constant-vector valueTopLeft))
        dotBottomRight (calc-dot-product bottomRight (get-constant-vector valueBottomRight))
        dotBottomLeft (calc-dot-product bottomleft (get-constant-vector valueBottomLeft))
        u (fade xf)
        v (fade yf)]

      (lin-interp u (lin-interp v dotBottomLeft dotTopLeft) (lin-interp v dotBottomRight dotTopRight))))


(defn do-octave [x y octaves frequency amplitude & [seed]]
  (let [seed-val (or seed nil)] 
    (loop [i 0
           n 0
           f frequency
           a amplitude]
      (if (= i octaves)
        n
        (recur (+ 1 i)
               (+ n (* a (perlin2D (* x f) (* y f) seed)))
               (* f 2)
               (* a 0.5))))))
