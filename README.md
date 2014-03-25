# urlMappingServer

server for URL mapping to entities

## Dependency

    <dependency>
      <groupId>de.metalcon</groupId>
      <artifactId>url-mapping-server</artifactId>
      <version>0.2.0</version>
    </dependency>

### Benchmark

[The benchmark](src/test/java/de/metalcon/urlmappingserver/LocalBenchmark.java) was executed with 2GB memory for the Java VM.

#### Write
>total number: 1,000,000  
>benchmark duration (write): 37794ms  
>per write: 37µs  
>writes per second: 26459.226332222046  

#### Read
>total number: 10,000,000  
>benchmark duration (read): 22584ms  
>per read: 2258ns  
>reads per second: 442791.356712717  
