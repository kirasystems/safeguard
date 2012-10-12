# safeguard

A Clojure library designed to ... well, that part is up to you.

## Other libraries

https://github.com/michaelklishin/validateur
https://github.com/weavejester/valip
https://github.com/joodie/clj-decline
https://github.com/mikejones/mississippi
https://github.com/noir-clojure/lib-noir

## Why?

1) Short-circuit validation. E.g. if the user id is not valid, do not
check the post id.

2) Play nicely with ring and compojure.

  wrap-validations
  with-validations

3) Transforms. (Optional)

 Validations should not transform data! True, but they often do 
 anyways while validating. The most common case is parsing
 integers. If a value is expected to be an integer, then every
 validation rule must first convert it to an integer before it can
 work with it. In this case, it makes sense to get over our idealism
 and just transform it on the first step.

 Another reason might be something as simple as marking data as 
 being validated. For example, when validating :params content,
 it is a good idea to copy validated data to a new key :safe-params.
 When validations are layered, this makes it clear to the final
 request handler what values have been validated. 

## Usage

FIXME

## License

Copyright © 2012 DiligenceEngine Inc.

Distributed under the Eclipse Public License, the same as Clojure.
