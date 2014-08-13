(ns rassilon.namespace-test
  (:require [midje.sweet :refer :all]))

(future-fact "Once a namespace is required, a fully qualified name can be used
to refer to members of the namespace"
  (rassilon.my-namespace/my-foo) => "foo")

(future-fact "Namespaces can be given a short name (alias) using :as"
  my-ns/my-foo => "foo")

(future-fact "Members of a namespace can be directly included using :refer"
  my-foo => "foo")
