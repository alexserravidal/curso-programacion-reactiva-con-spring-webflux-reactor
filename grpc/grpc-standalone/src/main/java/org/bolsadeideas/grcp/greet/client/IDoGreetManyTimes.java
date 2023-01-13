package org.bolsadeideas.grcp.greet.client;

import io.grpc.ManagedChannel;

public interface IDoGreetManyTimes {

    public void doGreetManyTimes(ManagedChannel channel, String name) throws Exception;

}
