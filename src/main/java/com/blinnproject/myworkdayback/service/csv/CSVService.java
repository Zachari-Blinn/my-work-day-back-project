package com.blinnproject.myworkdayback.service.csv;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

public interface CSVService {
  ByteArrayInputStream trainingSessionToCsv(Long createdBy, Date startDate, Date endDate) throws RuntimeException, IOException;
}
