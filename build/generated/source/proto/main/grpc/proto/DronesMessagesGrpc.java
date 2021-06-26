package proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: DronesMessages.proto")
public final class DronesMessagesGrpc {

  private DronesMessagesGrpc() {}

  public static final String SERVICE_NAME = "proto.DronesMessages";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneData,
      proto.DronesMessagesOuterClass.DroneData> getGreetMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "greet",
      requestType = proto.DronesMessagesOuterClass.DroneData.class,
      responseType = proto.DronesMessagesOuterClass.DroneData.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneData,
      proto.DronesMessagesOuterClass.DroneData> getGreetMethod() {
    io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneData, proto.DronesMessagesOuterClass.DroneData> getGreetMethod;
    if ((getGreetMethod = DronesMessagesGrpc.getGreetMethod) == null) {
      synchronized (DronesMessagesGrpc.class) {
        if ((getGreetMethod = DronesMessagesGrpc.getGreetMethod) == null) {
          DronesMessagesGrpc.getGreetMethod = getGreetMethod =
              io.grpc.MethodDescriptor.<proto.DronesMessagesOuterClass.DroneData, proto.DronesMessagesOuterClass.DroneData>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "greet"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.DroneData.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.DroneData.getDefaultInstance()))
              .setSchemaDescriptor(new DronesMessagesMethodDescriptorSupplier("greet"))
              .build();
        }
      }
    }
    return getGreetMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneStats,
      proto.DronesMessagesOuterClass.Empty> getSendStatsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sendStats",
      requestType = proto.DronesMessagesOuterClass.DroneStats.class,
      responseType = proto.DronesMessagesOuterClass.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneStats,
      proto.DronesMessagesOuterClass.Empty> getSendStatsMethod() {
    io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneStats, proto.DronesMessagesOuterClass.Empty> getSendStatsMethod;
    if ((getSendStatsMethod = DronesMessagesGrpc.getSendStatsMethod) == null) {
      synchronized (DronesMessagesGrpc.class) {
        if ((getSendStatsMethod = DronesMessagesGrpc.getSendStatsMethod) == null) {
          DronesMessagesGrpc.getSendStatsMethod = getSendStatsMethod =
              io.grpc.MethodDescriptor.<proto.DronesMessagesOuterClass.DroneStats, proto.DronesMessagesOuterClass.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sendStats"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.DroneStats.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DronesMessagesMethodDescriptorSupplier("sendStats"))
              .build();
        }
      }
    }
    return getSendStatsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneData,
      proto.DronesMessagesOuterClass.Empty> getElectionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "election",
      requestType = proto.DronesMessagesOuterClass.DroneData.class,
      responseType = proto.DronesMessagesOuterClass.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneData,
      proto.DronesMessagesOuterClass.Empty> getElectionMethod() {
    io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneData, proto.DronesMessagesOuterClass.Empty> getElectionMethod;
    if ((getElectionMethod = DronesMessagesGrpc.getElectionMethod) == null) {
      synchronized (DronesMessagesGrpc.class) {
        if ((getElectionMethod = DronesMessagesGrpc.getElectionMethod) == null) {
          DronesMessagesGrpc.getElectionMethod = getElectionMethod =
              io.grpc.MethodDescriptor.<proto.DronesMessagesOuterClass.DroneData, proto.DronesMessagesOuterClass.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "election"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.DroneData.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DronesMessagesMethodDescriptorSupplier("election"))
              .build();
        }
      }
    }
    return getElectionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneData,
      proto.DronesMessagesOuterClass.Empty> getElectedMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "elected",
      requestType = proto.DronesMessagesOuterClass.DroneData.class,
      responseType = proto.DronesMessagesOuterClass.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneData,
      proto.DronesMessagesOuterClass.Empty> getElectedMethod() {
    io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneData, proto.DronesMessagesOuterClass.Empty> getElectedMethod;
    if ((getElectedMethod = DronesMessagesGrpc.getElectedMethod) == null) {
      synchronized (DronesMessagesGrpc.class) {
        if ((getElectedMethod = DronesMessagesGrpc.getElectedMethod) == null) {
          DronesMessagesGrpc.getElectedMethod = getElectedMethod =
              io.grpc.MethodDescriptor.<proto.DronesMessagesOuterClass.DroneData, proto.DronesMessagesOuterClass.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "elected"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.DroneData.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DronesMessagesMethodDescriptorSupplier("elected"))
              .build();
        }
      }
    }
    return getElectedMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DeliveryInfo,
      proto.DronesMessagesOuterClass.Empty> getAssignDeliveryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "assignDelivery",
      requestType = proto.DronesMessagesOuterClass.DeliveryInfo.class,
      responseType = proto.DronesMessagesOuterClass.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DeliveryInfo,
      proto.DronesMessagesOuterClass.Empty> getAssignDeliveryMethod() {
    io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DeliveryInfo, proto.DronesMessagesOuterClass.Empty> getAssignDeliveryMethod;
    if ((getAssignDeliveryMethod = DronesMessagesGrpc.getAssignDeliveryMethod) == null) {
      synchronized (DronesMessagesGrpc.class) {
        if ((getAssignDeliveryMethod = DronesMessagesGrpc.getAssignDeliveryMethod) == null) {
          DronesMessagesGrpc.getAssignDeliveryMethod = getAssignDeliveryMethod =
              io.grpc.MethodDescriptor.<proto.DronesMessagesOuterClass.DeliveryInfo, proto.DronesMessagesOuterClass.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "assignDelivery"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.DeliveryInfo.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DronesMessagesMethodDescriptorSupplier("assignDelivery"))
              .build();
        }
      }
    }
    return getAssignDeliveryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneInfo,
      proto.DronesMessagesOuterClass.Empty> getSendDroneInfoToMasterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sendDroneInfoToMaster",
      requestType = proto.DronesMessagesOuterClass.DroneInfo.class,
      responseType = proto.DronesMessagesOuterClass.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneInfo,
      proto.DronesMessagesOuterClass.Empty> getSendDroneInfoToMasterMethod() {
    io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneInfo, proto.DronesMessagesOuterClass.Empty> getSendDroneInfoToMasterMethod;
    if ((getSendDroneInfoToMasterMethod = DronesMessagesGrpc.getSendDroneInfoToMasterMethod) == null) {
      synchronized (DronesMessagesGrpc.class) {
        if ((getSendDroneInfoToMasterMethod = DronesMessagesGrpc.getSendDroneInfoToMasterMethod) == null) {
          DronesMessagesGrpc.getSendDroneInfoToMasterMethod = getSendDroneInfoToMasterMethod =
              io.grpc.MethodDescriptor.<proto.DronesMessagesOuterClass.DroneInfo, proto.DronesMessagesOuterClass.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sendDroneInfoToMaster"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.DroneInfo.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DronesMessagesMethodDescriptorSupplier("sendDroneInfoToMaster"))
              .build();
        }
      }
    }
    return getSendDroneInfoToMasterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.Empty,
      proto.DronesMessagesOuterClass.Empty> getAliveMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "alive",
      requestType = proto.DronesMessagesOuterClass.Empty.class,
      responseType = proto.DronesMessagesOuterClass.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.Empty,
      proto.DronesMessagesOuterClass.Empty> getAliveMethod() {
    io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.Empty, proto.DronesMessagesOuterClass.Empty> getAliveMethod;
    if ((getAliveMethod = DronesMessagesGrpc.getAliveMethod) == null) {
      synchronized (DronesMessagesGrpc.class) {
        if ((getAliveMethod = DronesMessagesGrpc.getAliveMethod) == null) {
          DronesMessagesGrpc.getAliveMethod = getAliveMethod =
              io.grpc.MethodDescriptor.<proto.DronesMessagesOuterClass.Empty, proto.DronesMessagesOuterClass.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "alive"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DronesMessagesMethodDescriptorSupplier("alive"))
              .build();
        }
      }
    }
    return getAliveMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneData,
      proto.DronesMessagesOuterClass.Empty> getRemoveMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "remove",
      requestType = proto.DronesMessagesOuterClass.DroneData.class,
      responseType = proto.DronesMessagesOuterClass.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneData,
      proto.DronesMessagesOuterClass.Empty> getRemoveMethod() {
    io.grpc.MethodDescriptor<proto.DronesMessagesOuterClass.DroneData, proto.DronesMessagesOuterClass.Empty> getRemoveMethod;
    if ((getRemoveMethod = DronesMessagesGrpc.getRemoveMethod) == null) {
      synchronized (DronesMessagesGrpc.class) {
        if ((getRemoveMethod = DronesMessagesGrpc.getRemoveMethod) == null) {
          DronesMessagesGrpc.getRemoveMethod = getRemoveMethod =
              io.grpc.MethodDescriptor.<proto.DronesMessagesOuterClass.DroneData, proto.DronesMessagesOuterClass.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "remove"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.DroneData.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.DronesMessagesOuterClass.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DronesMessagesMethodDescriptorSupplier("remove"))
              .build();
        }
      }
    }
    return getRemoveMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DronesMessagesStub newStub(io.grpc.Channel channel) {
    return new DronesMessagesStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DronesMessagesBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new DronesMessagesBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DronesMessagesFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new DronesMessagesFutureStub(channel);
  }

  /**
   */
  public static abstract class DronesMessagesImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     *Metodo usato da un drone all'ingresso nella rete p2p
     * </pre>
     */
    public void greet(proto.DronesMessagesOuterClass.DroneData request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.DroneData> responseObserver) {
      asyncUnimplementedUnaryCall(getGreetMethod(), responseObserver);
    }

    /**
     * <pre>
     *Metodo per l'invio delle statistiche dei droni al master
     * </pre>
     */
    public void sendStats(proto.DronesMessagesOuterClass.DroneStats request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getSendStatsMethod(), responseObserver);
    }

    /**
     * <pre>
     *Metodo per effettuare l'elezione nell'anello
     * </pre>
     */
    public void election(proto.DronesMessagesOuterClass.DroneData request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getElectionMethod(), responseObserver);
    }

    /**
     * <pre>
     *Metodo per la notifica del nuovo drone master appena eletto
     * </pre>
     */
    public void elected(proto.DronesMessagesOuterClass.DroneData request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getElectedMethod(), responseObserver);
    }

    /**
     * <pre>
     *Metodo usato dal drone master per assegnare la consegna
     * </pre>
     */
    public void assignDelivery(proto.DronesMessagesOuterClass.DeliveryInfo request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getAssignDeliveryMethod(), responseObserver);
    }

    /**
     * <pre>
     *Metodo usato da un drone per mandare le proprie informazioni al master
     * </pre>
     */
    public void sendDroneInfoToMaster(proto.DronesMessagesOuterClass.DroneInfo request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getSendDroneInfoToMasterMethod(), responseObserver);
    }

    /**
     * <pre>
     *Metodo usato per controllare che il drone successivo nell'anello è alive
     * </pre>
     */
    public void alive(proto.DronesMessagesOuterClass.Empty request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getAliveMethod(), responseObserver);
    }

    /**
     * <pre>
     *Metodo usato per notificare i peer quando è stato rilevato un drone offline
     * </pre>
     */
    public void remove(proto.DronesMessagesOuterClass.DroneData request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getRemoveMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGreetMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.DronesMessagesOuterClass.DroneData,
                proto.DronesMessagesOuterClass.DroneData>(
                  this, METHODID_GREET)))
          .addMethod(
            getSendStatsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.DronesMessagesOuterClass.DroneStats,
                proto.DronesMessagesOuterClass.Empty>(
                  this, METHODID_SEND_STATS)))
          .addMethod(
            getElectionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.DronesMessagesOuterClass.DroneData,
                proto.DronesMessagesOuterClass.Empty>(
                  this, METHODID_ELECTION)))
          .addMethod(
            getElectedMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.DronesMessagesOuterClass.DroneData,
                proto.DronesMessagesOuterClass.Empty>(
                  this, METHODID_ELECTED)))
          .addMethod(
            getAssignDeliveryMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.DronesMessagesOuterClass.DeliveryInfo,
                proto.DronesMessagesOuterClass.Empty>(
                  this, METHODID_ASSIGN_DELIVERY)))
          .addMethod(
            getSendDroneInfoToMasterMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.DronesMessagesOuterClass.DroneInfo,
                proto.DronesMessagesOuterClass.Empty>(
                  this, METHODID_SEND_DRONE_INFO_TO_MASTER)))
          .addMethod(
            getAliveMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.DronesMessagesOuterClass.Empty,
                proto.DronesMessagesOuterClass.Empty>(
                  this, METHODID_ALIVE)))
          .addMethod(
            getRemoveMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                proto.DronesMessagesOuterClass.DroneData,
                proto.DronesMessagesOuterClass.Empty>(
                  this, METHODID_REMOVE)))
          .build();
    }
  }

  /**
   */
  public static final class DronesMessagesStub extends io.grpc.stub.AbstractStub<DronesMessagesStub> {
    private DronesMessagesStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DronesMessagesStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DronesMessagesStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DronesMessagesStub(channel, callOptions);
    }

    /**
     * <pre>
     *Metodo usato da un drone all'ingresso nella rete p2p
     * </pre>
     */
    public void greet(proto.DronesMessagesOuterClass.DroneData request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.DroneData> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGreetMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Metodo per l'invio delle statistiche dei droni al master
     * </pre>
     */
    public void sendStats(proto.DronesMessagesOuterClass.DroneStats request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendStatsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Metodo per effettuare l'elezione nell'anello
     * </pre>
     */
    public void election(proto.DronesMessagesOuterClass.DroneData request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getElectionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Metodo per la notifica del nuovo drone master appena eletto
     * </pre>
     */
    public void elected(proto.DronesMessagesOuterClass.DroneData request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getElectedMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Metodo usato dal drone master per assegnare la consegna
     * </pre>
     */
    public void assignDelivery(proto.DronesMessagesOuterClass.DeliveryInfo request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAssignDeliveryMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Metodo usato da un drone per mandare le proprie informazioni al master
     * </pre>
     */
    public void sendDroneInfoToMaster(proto.DronesMessagesOuterClass.DroneInfo request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendDroneInfoToMasterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Metodo usato per controllare che il drone successivo nell'anello è alive
     * </pre>
     */
    public void alive(proto.DronesMessagesOuterClass.Empty request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAliveMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *Metodo usato per notificare i peer quando è stato rilevato un drone offline
     * </pre>
     */
    public void remove(proto.DronesMessagesOuterClass.DroneData request,
        io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class DronesMessagesBlockingStub extends io.grpc.stub.AbstractStub<DronesMessagesBlockingStub> {
    private DronesMessagesBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DronesMessagesBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DronesMessagesBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DronesMessagesBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *Metodo usato da un drone all'ingresso nella rete p2p
     * </pre>
     */
    public proto.DronesMessagesOuterClass.DroneData greet(proto.DronesMessagesOuterClass.DroneData request) {
      return blockingUnaryCall(
          getChannel(), getGreetMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Metodo per l'invio delle statistiche dei droni al master
     * </pre>
     */
    public proto.DronesMessagesOuterClass.Empty sendStats(proto.DronesMessagesOuterClass.DroneStats request) {
      return blockingUnaryCall(
          getChannel(), getSendStatsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Metodo per effettuare l'elezione nell'anello
     * </pre>
     */
    public proto.DronesMessagesOuterClass.Empty election(proto.DronesMessagesOuterClass.DroneData request) {
      return blockingUnaryCall(
          getChannel(), getElectionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Metodo per la notifica del nuovo drone master appena eletto
     * </pre>
     */
    public proto.DronesMessagesOuterClass.Empty elected(proto.DronesMessagesOuterClass.DroneData request) {
      return blockingUnaryCall(
          getChannel(), getElectedMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Metodo usato dal drone master per assegnare la consegna
     * </pre>
     */
    public proto.DronesMessagesOuterClass.Empty assignDelivery(proto.DronesMessagesOuterClass.DeliveryInfo request) {
      return blockingUnaryCall(
          getChannel(), getAssignDeliveryMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Metodo usato da un drone per mandare le proprie informazioni al master
     * </pre>
     */
    public proto.DronesMessagesOuterClass.Empty sendDroneInfoToMaster(proto.DronesMessagesOuterClass.DroneInfo request) {
      return blockingUnaryCall(
          getChannel(), getSendDroneInfoToMasterMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Metodo usato per controllare che il drone successivo nell'anello è alive
     * </pre>
     */
    public proto.DronesMessagesOuterClass.Empty alive(proto.DronesMessagesOuterClass.Empty request) {
      return blockingUnaryCall(
          getChannel(), getAliveMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *Metodo usato per notificare i peer quando è stato rilevato un drone offline
     * </pre>
     */
    public proto.DronesMessagesOuterClass.Empty remove(proto.DronesMessagesOuterClass.DroneData request) {
      return blockingUnaryCall(
          getChannel(), getRemoveMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class DronesMessagesFutureStub extends io.grpc.stub.AbstractStub<DronesMessagesFutureStub> {
    private DronesMessagesFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DronesMessagesFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DronesMessagesFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DronesMessagesFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *Metodo usato da un drone all'ingresso nella rete p2p
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.DronesMessagesOuterClass.DroneData> greet(
        proto.DronesMessagesOuterClass.DroneData request) {
      return futureUnaryCall(
          getChannel().newCall(getGreetMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Metodo per l'invio delle statistiche dei droni al master
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.DronesMessagesOuterClass.Empty> sendStats(
        proto.DronesMessagesOuterClass.DroneStats request) {
      return futureUnaryCall(
          getChannel().newCall(getSendStatsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Metodo per effettuare l'elezione nell'anello
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.DronesMessagesOuterClass.Empty> election(
        proto.DronesMessagesOuterClass.DroneData request) {
      return futureUnaryCall(
          getChannel().newCall(getElectionMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Metodo per la notifica del nuovo drone master appena eletto
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.DronesMessagesOuterClass.Empty> elected(
        proto.DronesMessagesOuterClass.DroneData request) {
      return futureUnaryCall(
          getChannel().newCall(getElectedMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Metodo usato dal drone master per assegnare la consegna
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.DronesMessagesOuterClass.Empty> assignDelivery(
        proto.DronesMessagesOuterClass.DeliveryInfo request) {
      return futureUnaryCall(
          getChannel().newCall(getAssignDeliveryMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Metodo usato da un drone per mandare le proprie informazioni al master
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.DronesMessagesOuterClass.Empty> sendDroneInfoToMaster(
        proto.DronesMessagesOuterClass.DroneInfo request) {
      return futureUnaryCall(
          getChannel().newCall(getSendDroneInfoToMasterMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Metodo usato per controllare che il drone successivo nell'anello è alive
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.DronesMessagesOuterClass.Empty> alive(
        proto.DronesMessagesOuterClass.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(getAliveMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     *Metodo usato per notificare i peer quando è stato rilevato un drone offline
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.DronesMessagesOuterClass.Empty> remove(
        proto.DronesMessagesOuterClass.DroneData request) {
      return futureUnaryCall(
          getChannel().newCall(getRemoveMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GREET = 0;
  private static final int METHODID_SEND_STATS = 1;
  private static final int METHODID_ELECTION = 2;
  private static final int METHODID_ELECTED = 3;
  private static final int METHODID_ASSIGN_DELIVERY = 4;
  private static final int METHODID_SEND_DRONE_INFO_TO_MASTER = 5;
  private static final int METHODID_ALIVE = 6;
  private static final int METHODID_REMOVE = 7;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DronesMessagesImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DronesMessagesImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GREET:
          serviceImpl.greet((proto.DronesMessagesOuterClass.DroneData) request,
              (io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.DroneData>) responseObserver);
          break;
        case METHODID_SEND_STATS:
          serviceImpl.sendStats((proto.DronesMessagesOuterClass.DroneStats) request,
              (io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty>) responseObserver);
          break;
        case METHODID_ELECTION:
          serviceImpl.election((proto.DronesMessagesOuterClass.DroneData) request,
              (io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty>) responseObserver);
          break;
        case METHODID_ELECTED:
          serviceImpl.elected((proto.DronesMessagesOuterClass.DroneData) request,
              (io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty>) responseObserver);
          break;
        case METHODID_ASSIGN_DELIVERY:
          serviceImpl.assignDelivery((proto.DronesMessagesOuterClass.DeliveryInfo) request,
              (io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty>) responseObserver);
          break;
        case METHODID_SEND_DRONE_INFO_TO_MASTER:
          serviceImpl.sendDroneInfoToMaster((proto.DronesMessagesOuterClass.DroneInfo) request,
              (io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty>) responseObserver);
          break;
        case METHODID_ALIVE:
          serviceImpl.alive((proto.DronesMessagesOuterClass.Empty) request,
              (io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty>) responseObserver);
          break;
        case METHODID_REMOVE:
          serviceImpl.remove((proto.DronesMessagesOuterClass.DroneData) request,
              (io.grpc.stub.StreamObserver<proto.DronesMessagesOuterClass.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class DronesMessagesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DronesMessagesBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return proto.DronesMessagesOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DronesMessages");
    }
  }

  private static final class DronesMessagesFileDescriptorSupplier
      extends DronesMessagesBaseDescriptorSupplier {
    DronesMessagesFileDescriptorSupplier() {}
  }

  private static final class DronesMessagesMethodDescriptorSupplier
      extends DronesMessagesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    DronesMessagesMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DronesMessagesGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DronesMessagesFileDescriptorSupplier())
              .addMethod(getGreetMethod())
              .addMethod(getSendStatsMethod())
              .addMethod(getElectionMethod())
              .addMethod(getElectedMethod())
              .addMethod(getAssignDeliveryMethod())
              .addMethod(getSendDroneInfoToMasterMethod())
              .addMethod(getAliveMethod())
              .addMethod(getRemoveMethod())
              .build();
        }
      }
    }
    return result;
  }
}
