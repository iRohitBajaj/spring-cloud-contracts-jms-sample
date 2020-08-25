package contracts.beer.messaging

import org.springframework.cloud.contract.spec.Contract
[
Contract.make {
    description("""
Sends a positive verification message when person is eligible to get the beer

```
given:
	client is old enough
when:
	he applies for a beer
then:
	we'll send a message with a true
```

""")
    // Label by means of which the output message can be triggered
    label 'allow_beer'
    input {
        messageFrom(value(consumer("req2.q"), producer('req.q')))
        messageBody('''
{
	"name": "Jack",
	"age": 34
}
''')
        messageHeaders {
            header("_type", "com.example.BeerRequest")
            header("jms_replyTo", "resp2.q")
            header("jms_messageId", "allowMessage")
        }

    }
    // output message of the contract
    outputMessage {
        // destination to which the output message will be sent
        sentTo 'resp2.q'
        // the body of the output message
        body(
                eligible: true
        )
        headers{
            header("jms_correlationId", "allowMessage")
            header("_type", "com.example.BeerResponse")
        }
    }
},
Contract.make {
    description("""
Sends a negative verification message when person is not eligible to get the beer

```
given:
	client is too young
when:
	he applies for a beer
then:
	we'll send a message with a false
```

""")
    // Label by means of which the output message can be triggered
    label 'deny_beer'
    // input to the contract
    input {
        messageFrom(value(consumer("req1.q"), producer('req.q')))
        messageBody('''
{
	"name": "Chris",
	"age": 14
}
''')
        messageHeaders {
            header("_type", "com.example.BeerRequest")
            header("jms_replyTo", "resp1.q")
            header("jms_messageId", "denyMessage")
        }
    }
    // output message of the contract
    outputMessage {
        // destination to which the output message will be sent
        sentTo 'resp1.q'
        // the body of the output message
        body(
                eligible: false
        )
        headers{
            header("jms_correlationId", "denyMessage")
            header("_type", "com.example.BeerResponse")
        }
    }
}]
