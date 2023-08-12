// package com.aptech.coursemanagementserver.controllers;

// import static
// com.aptech.coursemanagementserver.constants.GlobalStorage.GLOBAL_EXCEPTION;
// import static
// com.aptech.coursemanagementserver.constants.GlobalStorage.MOMO_REDIRECT_URL;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.servlet.view.RedirectView;

// import com.aptech.coursemanagementserver.dtos.payment.MomoRequestDto;
// import com.aptech.coursemanagementserver.dtos.payment.MomoResponseDto;
// import com.aptech.coursemanagementserver.exceptions.BadRequestException;
// import
// com.aptech.coursemanagementserver.services.paymentServices.MomoService;

// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import lombok.RequiredArgsConstructor;

// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/momo")
// @Tag(name = "Momo Payment Endpoints")
// public class MomoController {
// private final MomoService momoService;

// @GetMapping(path = "/")
// public RedirectView home() {
// return new RedirectView("https://momo.vn");
// }

// @PostMapping
// @Operation(summary = "[USER] - Initiate payment MOMO")
// // @PreAuthorize("hasAnyRole('USER')")
// public ResponseEntity<MomoResponseDto> initiatePaymentMomo(@RequestBody
// MomoRequestDto momoRequestDto)
// throws Exception {
// try {
// MomoResponseDto response = momoService.initPaymentMomo(momoRequestDto);
// // if (response.getResultCode() == 0 && response.getPayUrl().length() > 0) {
// // return
// //
// ResponseEntity.status(HttpStatus.FOUND).location(URI.create(response.getPayUrl()))
// // .build();
// // }
// if (response.getResultCode() == 0 && response.getPayUrl().length() > 0) {
// return ResponseEntity.ok(response);
// }
// throw new BadRequestException(GLOBAL_EXCEPTION);
// } catch (Exception e) {
// throw new BadRequestException(GLOBAL_EXCEPTION);
// }

// }

// @GetMapping(path = MOMO_REDIRECT_URL)
// @Operation(summary = "[ANORNYMOUS] - Redirect from MOMO")
// public RedirectView redirect(@RequestParam int resultCode, @RequestParam
// String extraData) {
// momoService.UpdateOrderAndCreateEnroll(resultCode, extraData);
// if (resultCode == 0 || resultCode == 9000) {

// return new RedirectView("http://localhost:3000/payment/success");
// }
// // 1001: Giao dịch thanh toán thất bại do tài khoản người dùng không đủ tiền.
// // 1005: Giao dịch thất bại do url hoặc QR code đã hết hạn.
// // 1006: Giao dịch thất bại do người dùng đã từ chối xác nhận thanh toán.
// if (resultCode == 1001) {
// // momoService.UpdateOrderAndCreateEnroll(resultCode, extraData);
// return new
// RedirectView("http://localhost:3000/payment/cancel?resultCode=1001");
// } else if (resultCode == 1005) {
// // momoService.UpdateOrderAndCreateEnroll(resultCode, extraData);
// return new
// RedirectView("http://localhost:3000/payment/cancel?resultCode=1005");
// } else if (resultCode == 1006) {
// // momoService.UpdateOrderAndCreateEnroll(resultCode, extraData);
// return new
// RedirectView("http://localhost:3000/payment/cancel?resultCode=1006");
// } else {
// resultCode = 9999;
// momoService.UpdateOrderAndCreateEnroll(resultCode, extraData);
// return new RedirectView("http://localhost:3000/payment/cancel");
// }

// }

// // @GetMapping(path = PAYPAL_SUCCESS_URL)
// // public RedirectView successPay(@RequestParam("paymentId") String
// paymentId,
// // @RequestParam("PayerID") String payerId) {
// // try {
// // Payment payment = service.executePayment(paymentId, payerId);
// // System.out.println(payment.toJSON());
// // if (payment.getState().equals("approved")) {
// // service.createOrderAndEnroll(payment);
// // return new RedirectView(PAYPAL_SUCCESS_CLIENT);
// // }
// // return new RedirectView(PAYPAL_CANCEL_CLIENT);
// // } catch (PayPalRESTException e) {
// // throw new BadRequestException(e.getMessage());
// // } catch (Exception e) {
// // throw new BadRequestException(GLOBAL_EXCEPTION);
// // }
// // }

// }
