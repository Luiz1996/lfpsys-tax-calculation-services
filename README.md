This is the Tax Calculation Control Service of LFPSys.

#### Main Technology Stack
* Java 21
* PostgreSQL
* Apache Kafka
* Redis
* Maven 3.9.1 (or higher)
* [Spring Boot Framework](https://spring.io/projects/spring-boot)

#### Communication Contracts
##### KAFKA
	Tópico: tax-calculation
	Headers:
		client_id: <UUID>
	Payload:
		{
			"nfeId": <UUID>,
			"taxCalculations": [
				{
					"taxId": <UUID>,
					"operation": <Enum>, //ADD ou SUBTRACT
					"value": "123.45"
				},
				{
					"taxId": <UUID>,
					"operation": <Enum>, //ADD ou SUBTRACT
					"value": "123.45"
				}
			]
		}