Narrative:
In order to Goal 2
As a beacon api user
I want to count the number of characters in the OutputValue the beacon returns.

Scenario: 002. Collect characters for time period
GivenStories:com/conductor/preconditions/getOutputValue.story
Then print counter of characters with format '{character}, {counter}' from '3 months 1 day 1 hour 45 minutes ago' to '3 months 1 day 1 hour 40 minutes ago'