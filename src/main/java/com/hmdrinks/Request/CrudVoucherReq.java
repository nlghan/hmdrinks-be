package com.hmdrinks.Request;

import com.hmdrinks.Enum.Status_Voucher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class CrudVoucherReq {
    private int voucherId;
    private Date startDate;
    private Date endDate;
    private Double discount;
    private Status_Voucher status;
    private int postId;
}
