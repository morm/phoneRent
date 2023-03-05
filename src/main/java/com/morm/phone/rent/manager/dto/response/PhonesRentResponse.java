package com.morm.phone.rent.manager.dto.response;

import com.morm.phone.rent.manager.dto.PhoneRentItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhonesRentResponse implements Serializable {

  private List<PhoneRentItem> phonesRent = new ArrayList<>();
}
