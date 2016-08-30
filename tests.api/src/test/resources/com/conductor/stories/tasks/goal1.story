Narrative:
In order to Goal 1
As a beacon api user
I want to count the number of characters in the OutputValue the beacon returns.

Scenario: 001. Count the number of characters
GivenStories:com/conductor/preconditions/getOutputValue.story
Then print counter of characters with format '{character}, {counter}'