Feature: Test Toxcast model retrieval

  Scenario: Testing a GET endpoint with DTXSID as a search parameter
    Given url 'http://localhost:9300/toxcast-model/search/by-dtxsid?dtxsid=DTXSID001001267'
    When method GET
    Then status 200
    And match $ contains {content: '#notnull'}

  Scenario: Testing a GET endpoint with an invalid DTXSID
    Given url 'http://localhost:9300/toxcast-model/search/by-dtxsid?dtxsid='
    When method GET
    Then status 200
    And assert response.content.length === 1
