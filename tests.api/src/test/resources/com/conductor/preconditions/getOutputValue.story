Narrative:
In order to Goal 1
As a beacon api user
I want to get the OutputValue from beacon.

Scenario: 000. Get output value
Given url for beacon api calls is 'https://beacon.nist.gov/rest/record'
And root element is 'record'
And I am going to parse 'outputValue' field
When the most recent event from the randomness beacon was retrieved