(ns rassilon.variables-test
  (:require [midje.sweet :refer :all]))

(future-fact "def binds vars"
  (def foo 1) => #'foo  ;; Hopefully the result seems odd - we'll come back to it

  foo => ?????)

(future-fact "Note that vars are _not_ lexically scoped - def 'modifies' the current
namespace, so we can refer to foo here too."
  foo => ?????)

(fact "when we reference a var by its symbol name we get the value contained
in the var, rather than the var itself. If we want the var we can use `#'`.
You _mostly_ won't care about this in 'business' logic, but the fact that vars
are first class, and distinct from the values they point to enables a number
of programming techniques such as thread local variables and aspect oriented
programming."
  (var? foo)   => false
  (var? #'foo) => true)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Let bindings
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(future-fact "`let` introduces local, lexically scoped 'variables'."
  (let [a 1]
    a => ?????)

  (let [a "foo"]
    a => ?????)
  
  (let [a 1
        b 2]
    (+ a b) => ?????))

(future-fact "because they're lexically scoped, they nest the way you'd expect"
  (let [a 1
        b 2
        c 3]
    (let [a 4]               ;; Shadows (does not change!) `a` from above
      (let [b ?????]         ;; Shadows (does not change!) `b` from above
        (+ a b c) => 12)))

  (let [a 1
        b 2]
    (let [a (+ a 1)          ;; We can even reference the outer `a`
          b (+ b 2)]         ;; Same for `b`
      (+ a b) => ?????)))

(future-fact "`let` bindings shadow global vars (from `def`)"
  (let [foo 1 bar 2]
    (+ foo bar) => ?????))


