package org.immregistries.hart.cdc;

@SuppressWarnings("serial")
public class UnsupportedOperationFault extends Fault {
  public UnsupportedOperationFault(String message) {
    super(message, FaultDetail.UNSUPPORTED_OPERATION);
  }
}
