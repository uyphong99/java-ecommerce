package com.shopme.common.entity.order;


import jakarta.persistence.*;
import lombok.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "order_track")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OrderTrack  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 256)
	private String notes;
	private LocalDateTime updatedTime;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 45, nullable = false)
	private OrderStatus status;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	
	@Transient
	public String getUpdatedTimeOnForm() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss");
		return dateFormatter.format(this.updatedTime);
	}
	
	public void setUpdatedTimeOnForm(String dateString) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss");

		this.updatedTime = LocalDateTime.parse(dateString, dateFormatter);;
	}

}
