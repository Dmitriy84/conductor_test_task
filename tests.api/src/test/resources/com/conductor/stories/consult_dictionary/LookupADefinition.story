Narrative:
In order to Goal 1: Sampling Chaos
As a test automation engineer
I want to count the number of characters in the OutputValue the beacon returns.

Scenario: Count the number of characters
Given url for beacon api calls is 'https://beacon.nist.gov/rest/record'
And root element is 'record'
And I am going to parse 'outputValue' field
When the most recent event from the randomness beacon was retrieved
Then print counter of characters with format '{character}, {counter}'

Scenario: Collect characters for time period
Then print counter of characters with format '{character}, {counter}' from '3 months 1 day 1 hour 45 minutes ago' to '3 months 1 day 1 hour 40 minutes ago'