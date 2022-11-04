# My Backend Roadmap
## Apuntes
- Eureka NO sirve de balanceador de cargas, es sólo un servicio de registro de nombre
- Sin embargo, en SpringCloud Gateway se usa Spring Cloud Load Balancer por defecto
- Así como el Cliente Feign y RestTemplate que se usan en el MS de Items (Desconozco si al usar @EnableFeignClients o al tener la dependencia de SpringCloud en el microservicio)
- Load Balance
    - Ribbon ya no se usa pero va bien saber usarlo porque hay proyectos antiguos que lo utilizan
    - Spring Cloud Load Balancer se usa actualmente
- Circuit Breaker
    - Hystrix ya no se usa pero va bien saber usarlo porque hay proyectos antiguos que lo utilizan
    - Resilience4j se usa actualmente