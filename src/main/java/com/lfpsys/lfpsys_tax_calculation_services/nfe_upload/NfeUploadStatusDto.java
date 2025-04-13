package com.lfpsys.lfpsys_tax_calculation_services.nfe_upload;

import java.util.List;

public class NfeUploadStatusDto {

  private List<NfeUploadProcessDto> processes;

  public NfeUploadStatusDto() {}

  public NfeUploadStatusDto(final List<NfeUploadProcessDto> processes) {
    this.processes = processes;
  }

  public List<NfeUploadProcessDto> getProcesses() {
    return processes;
  }

  public void setProcesses(final List<NfeUploadProcessDto> processes) {
    this.processes = processes;
  }
}
