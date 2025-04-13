package com.lfpsys.lfpsys_tax_calculation_services.nfe_upload;

public class NfeUploadProcessDto {

  private NfeUploadProcessType process;
  private NfeUploadProcessStatus status;

  public NfeUploadProcessDto(final NfeUploadProcessType process, final NfeUploadProcessStatus status) {
    this.process = process;
    this.status = status;
  }

  public NfeUploadProcessType getProcess() {
    return process;
  }

  public void setProcess(final NfeUploadProcessType process) {
    this.process = process;
  }

  public NfeUploadProcessStatus getStatus() {
    return status;
  }

  public void setStatus(final NfeUploadProcessStatus status) {
    this.status = status;
  }
}
