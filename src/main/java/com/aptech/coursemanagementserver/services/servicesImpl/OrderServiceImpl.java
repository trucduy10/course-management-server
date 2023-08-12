package com.aptech.coursemanagementserver.services.servicesImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.InvoiceDto;
import com.aptech.coursemanagementserver.dtos.OrderDto;
import com.aptech.coursemanagementserver.dtos.OrderHistoryRequestDto;
import com.aptech.coursemanagementserver.models.Orders;
import com.aptech.coursemanagementserver.repositories.OrdersRepository;
import com.aptech.coursemanagementserver.services.OrderService;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrdersRepository orderRepository;

    @Override
    public OrderDto findById(long id) {
        Orders order = orderRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(
                        "This order with orderId: [" + id + "] is not exist."));

        return toDto(order);
    }

    @Override
    public Page<OrderDto> findByUserId(OrderHistoryRequestDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageNo(), dto.getPageSize());
        Page<Orders> orders = orderRepository.findByUserId(dto.getUserId(), pageable);
        List<OrderDto> orderDtos = new ArrayList<>();

        for (Orders order : orders.getContent()) {
            OrderDto orderDto = toDto(order);
            orderDtos.add(orderDto);
        }
        Page<OrderDto> pageOrderDtos = new PageImpl<>(orderDtos, orders.getPageable(), orders.getTotalElements());
        return pageOrderDtos;
    }

    @Override
    public Page<OrderDto> findInCompletedByUserId(OrderHistoryRequestDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageNo(), dto.getPageSize());
        Page<Orders> orders = orderRepository.findInCompletedByUserId(dto.getUserId(), pageable);
        List<OrderDto> orderDtos = new ArrayList<>();

        for (Orders order : orders.getContent()) {
            OrderDto orderDto = toDto(order);
            orderDtos.add(orderDto);
        }
        Page<OrderDto> pageOrderDtos = new PageImpl<>(orderDtos, orders.getPageable(), orders.getTotalElements());
        return pageOrderDtos;
    }

    @Override
    public OrderDto findByTransactionId(String transactionId) {
        Orders order = orderRepository.findByTransactionId(transactionId).orElseThrow(
                () -> new NoSuchElementException(
                        "This order with transactionId: [" + transactionId + "] is not exist."));

        return toDto(order);
    }

    @Override
    public List<OrderDto> findAll() {
        List<Orders> orders = orderRepository.findAll();

        List<OrderDto> orderDtos = new ArrayList<>();

        for (Orders order : orders) {
            OrderDto orderDto = toDto(order);
            orderDtos.add(orderDto);
        }
        return orderDtos;
    }

    private OrderDto toDto(Orders order) {
        OrderDto orderDto = OrderDto.builder()
                .id(order.getId())
                .userName(order.getUser().getName())
                .courseName(order.getCourse().getName())
                .image(order.getImage())
                .slug(order.getCourse().getSlug())
                .transactionId(order.getTransactionId())
                .description(order.getDescription())
                .userDescription(order.getUserDescription())
                .duration(order.getDuration())
                .price(order.getPrice())
                .net_price(order.getNet_price())
                .payment(order.getPayment())
                .status(order.getStatus())
                .created_at(order.getCreated_at())
                .build();
        return orderDto;
    }

    @Override
    public byte[] getInvoice(InvoiceDto invoiceDto) throws JRException, IOException {
        JasperReport jasperReport = JasperCompileManager
                .compileReport(new ClassPathResource("reports/Invoice_Landscape.jrxml").getInputStream());

        List<InvoiceDto> invoiceDtos = new ArrayList<>();
        invoiceDtos.add(invoiceDto);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(invoiceDtos);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
