# urlMappingServer

server for URL mapping to entities

## Dependency

    <dependency>
      <groupId>de.metalcon</groupId>
      <artifactId>url-mapping-server</artifactId>
      <version>0.2.0</version>
    </dependency>

## Benchmark

[The benchmark](src/test/java/de/metalcon/urlmappingserver/Benchmark.java) was executed with 2GB memory for the Java VM.

### Benchmark without persistence layer

#### Write
>total number: 500,000  
>benchmark duration (write): 18754ms  
>per write: 37µs  
>writes per second: 26660.978991148557  


#### Read
>total number: 5,000,000  
>benchmark duration (read): 9136ms  
>per read: 1827ns  
>reads per second: 547285.4640980735  

### Benchmark with persistence layer (levelDB)

#### Write (call API locally)
>total number: 500,000  
>benchmark duration (write): 28946ms  
>per write: 57µs  
>writes per second: 17273.543840254268  

#### Read (call API locally)
>total number: 5,000,000  
>benchmark duration (read): 9841ms  
>per read: 1968ns  
>reads per second: 508078.44731226505  

The server was stopped and restarted, making it load all mappings from disk.

#### Write (restore from disk)
>total number: 500,000  
>benchmark duration (restart): 31348ms  
>per write: 62µs  
>writes per second: 15949.980860022968  

#### Read (call API locally)
>total number: 5,000,000  
>benchmark duration (read): 13263ms  
>per read: 2652ns  
>reads per second: 376988.61494382867  

