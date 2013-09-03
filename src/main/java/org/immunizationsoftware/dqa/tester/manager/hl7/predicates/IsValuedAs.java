package org.immunizationsoftware.dqa.tester.manager.hl7.predicates;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class IsValuedAs extends ConditionalPredicate
{

  private String[] values;

  public IsValuedAs(HL7Component component, String value) {
    this(component, value, UsageType.O, UsageType.O);
  }

  public IsValuedAs(HL7Component component, String value, UsageType usageTypeMet, UsageType usageTypeNotMet) {
    super(component, usageTypeMet, usageTypeNotMet);
    values = new String[1];
    values[0] = value;
  }

  public IsValuedAs(HL7Component component, String[] values, UsageType usageTypeMet, UsageType usageTypeNotMet) {
    super(component, usageTypeMet, usageTypeNotMet);
    this.values = values;
  }

  @Override
  public boolean isMet() {
    if (component.getValue() == null) {
      return false;
    }
    for (String value : values) {
      if (component.getValue().equalsIgnoreCase(value)) {
        return true;
      }
    }
    return false;
  }

}
