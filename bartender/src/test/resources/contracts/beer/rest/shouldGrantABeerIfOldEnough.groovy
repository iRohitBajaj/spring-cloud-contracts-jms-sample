
package contracts.beer.rest

import org.springframework.cloud.contract.spec.Contract
Contract.make {
	description("""
Represents a successful scenario of getting a beer

```
given:
	client is old enough
when:
	he applies for a beer
then:
	we'll grant him the beer
```

""")
	request {
		method POST()
		url '/'
		body(
				age: $(regex("[2-9][0-9]"))
		)
		headers {
			contentType(applicationJson())
		}
	}
	response {
		status 200
		body("""
			{
				"eligible": true
			}
			""")
		headers {
			contentType(applicationJson())
		}
	}
}
