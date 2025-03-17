package org.immregistries.hart.util;

// For working with HL7 format birth dates: YYYYMMDD
public class DateOfBirth {
  private String dobString = "";
  private int day = 0;
  private int month = 0;
  private int year = 1900;

  public DateOfBirth() {
    this.dobString = StringManipulation.dateFromInts(this.day, this.month, this.year);
  }

  public DateOfBirth(int day, int month, int year) {
    this.dobString = StringManipulation.dateFromInts(day, month, year);
    this.day = day;
    this.month = month;
    this.year = year;
  }

  public DateOfBirth(String dob) {
    if (dob.length() < 8) {
      this.dobString = dob;
      return;
    }

    try {
      this.year = Integer.parseInt(dob.substring(0, 4));
      this.month = Integer.parseInt(dob.substring(4, 6));
      this.day = Integer.parseInt(dob.substring(6, 8));
    } catch (NumberFormatException ignore) { }
    this.dobString = dob;
  }

  private boolean valid() {
    return this.day != 0 && this.month != 0 && this.year != 1900;
  }

  public String decrementDay() {
    if (!this.valid()) { return this.dobString; }
    if (this.dobString.length() >= 8) {
      if (this.dobString.length() > 8) {
        this.dobString = this.dobString.substring(0, 8);
      }

      if (this.day > 0 && this.month > 0 && this.year > 1900) {
        if (this.day == 1) {
          this.day = 28;
          this.month = this.month - 1;
          if (this.month < 0) {
            this.year = this.year - 1;
            this.month = this.month + 12;
          }
        } else {
          this.day = this.day - 1;
        }

        this.dobString = StringManipulation.dateFromInts(this.year, this.month, this.day);
      }
    }
    return this.dobString;
  }

  public String decrementMonth() {
    if (!this.valid()) { return this.dobString; }
    if (this.dobString.length() >= 8) {
      if (this.dobString.length() > 8) {
        this.dobString = this.dobString.substring(0, 8);
      }

      if (this.day > 0 && this.month > 0 && this.year > 1900) {
        this.month = this.month - 1;
        if (this.month < 1) {
          this.month = this.month + 12;
          this.year = this.year - 1;
        }
        if (this.day > 28) {
          this.day = 28;
        }

        this.dobString = StringManipulation.dateFromInts(this.year, this.month, this.day);
      }
    }
    return this.dobString;
  }

  public String decrementYear() {
    if (!this.valid()) { return this.dobString; }
    if (this.dobString.length() >= 8) {
      if (this.dobString.length() > 8) {
        this.dobString = this.dobString.substring(0, 8);
      }

      if (this.day > 0 && this.month > 0 && this.year > 1900) {
        this.year = this.year - 1;
        if (this.month == 2 && this.day == 29) {
          this.day = 28;
        }

        this.dobString = StringManipulation.dateFromInts(this.year, this.month, this.day);
      }
    }
    return this.dobString;
  }

  public String swapDayAndMonth() {
    if (!this.valid()) { return this.dobString; }
    if (this.dobString.length() >= 8) {
      if (this.dobString.length() > 8) {
        this.dobString = this.dobString.substring(0, 8);
      }

      if (this.day > 0 && this.month > 0 && this.year > 1900) {
        if (this.day > 12) {
          // If the day is too high then we have to move the date of birth back in time
          // to a day of the month that is 12 or lower. We will shift it back and then
          // adjust the date so that it is still accurate.
          if (this.day > 24) {
            this.month = this.month - 2;
            this.day = this.day - 24;
          } else {
            this.month = this.month - 1;
            this.day = this.day - 12;
          }
          if (this.month < 1) {
            // month has decremented into next year
            this.month = this.month + 12;
            this.year = this.year - 1;
          }
        } else {
          // We are good to do a switch of dates
          int temp = this.month;
          this.month = this.day;
          this.day = temp;
        }

        this.dobString = StringManipulation.dateFromInts(this.year, this.month, this.day);
      }
    }
    return this.dobString;
  }

  public static String rectify(String dob) {
    if (dob.length() >= 8) {
      if (dob.length() > 8) {
        dob = dob.substring(0, 8);
      }
      int year = 0;
      int month = 0;
      int day = 0;
      try {
        year = Integer.parseInt(dob.substring(0, 4));
        month = Integer.parseInt(dob.substring(4, 6));
        day = Integer.parseInt(dob.substring(6, 8));
      } catch (NumberFormatException ignore) {
        day = 0;
      }

      if (day > 0 && month > 0 && year > 1900) {
        if (day < 3) {
          // don't leave day on the first two days of the month, move back to the previous month
          month = month - 1;
          if (month < 1) {
            // month has decremented into next year
            month = month + 12;
            year = year - 1;
          }
          day = 28 - (day - 1);
        } else if (day > 28) {
          // don't leave the date on the days that don't work on all months, move backwards into month
          day = 28 - (31 - day);
        }
        dob = StringManipulation.dateFromInts(year, month, day);
      }
    }
    return dob;
  }

  public String setDay(int newDay) {
    if (day < 0 && day > 31) { return this.dobString; }
    this.day = newDay;
    this.dobString = StringManipulation.dateFromInts(this.year, this.month, this.day);
    return this.dobString;
  }

  public String setDay(String newDay) {
    int originalDay = this.day;
    try {
      this.day = Integer.parseInt(newDay);
    } catch (NumberFormatException ignore) { return this.dobString; }
    if (this.day < 0 && this.day > 31) {
      this.day = originalDay;
      return this.dobString;
    }
    this.dobString = StringManipulation.dateFromInts(this.year, this.month, this.day);
    return this.dobString;
  }

  public String setMonth(int newMonth) {
    if (newMonth < 0 && newMonth > 12) { return this.dobString; }
    this.month = newMonth;
    this.dobString = StringManipulation.dateFromInts(this.year, this.month, this.day);
    return this.dobString;
  }

  public String setMonth(String newMonth) {
    int originalmonth = this.month;
    try {
      this.month = Integer.parseInt(newMonth);
    } catch (NumberFormatException ignore) { return this.dobString; }
    if (this.month < 0 && this.month > 12) {
      this.month = originalmonth;
      return this.dobString;
    }
    this.dobString = StringManipulation.dateFromInts(this.year, this.month, this.day);
    return this.dobString;
  }

  public String setYear(int newYear) {
    if (newYear < 1000) { return this.dobString; }
    this.year = newYear;
    this.dobString = StringManipulation.dateFromInts(this.year, this.month, this.day);
    return this.dobString;
  }

  public String setYear(String newYear) {
    int originalyear = this.year;
    try {
      this.year = Integer.parseInt(newYear);
    } catch (NumberFormatException ignore) { return this.dobString; }
    if (this.year < 0 && this.year > 31) {
      this.year = originalyear;
      return this.dobString;
    }
    this.dobString = StringManipulation.dateFromInts(this.year, this.month, this.day);
    return this.dobString;
  }
}
