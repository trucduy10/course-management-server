// package com.aptech.coursemanagementserver.controllers;

// import static
// com.aptech.coursemanagementserver.constants.GlobalStorage.GLOBAL_EXCEPTION;
// import static
// com.aptech.coursemanagementserver.constants.GlobalStorage.PAYPAL_CANCEL_API;
// import static
// com.aptech.coursemanagementserver.constants.GlobalStorage.PAYMENT_SUCCESS_CLIENT;
// import static
// com.aptech.coursemanagementserver.constants.GlobalStorage.PAYPAL_CANCEL_URL;
// import static
// com.aptech.coursemanagementserver.constants.GlobalStorage.PAYPAL_SUCCESS_API;
// import static
// com.aptech.coursemanagementserver.constants.GlobalStorage.PAYMENT_CANCEL_CLIENT;
// import static
// com.aptech.coursemanagementserver.constants.GlobalStorage.PAYPAL_SUCCESS_URL;

// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.servlet.view.RedirectView;

// import com.aptech.coursemanagementserver.dtos.payment.PaypalRequestDto;
// import com.aptech.coursemanagementserver.dtos.payment.PaypalResponseDto;
// import com.aptech.coursemanagementserver.exceptions.BadRequestException;
// import
// com.aptech.coursemanagementserver.services.paymentServices.PaypalService;
// import com.paypal.api.payments.Links;
// import com.paypal.api.payments.Payment;
// import com.paypal.base.rest.PayPalRESTException;

// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import lombok.RequiredArgsConstructor;

// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/paypal")
// @Tag(name = "Paypal Payment Endpoints")

// public class PaypalController {
// private final PaypalService service;

// @GetMapping(path = "/")
// public RedirectView home() {
// return new RedirectView(
// "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-6G2428565Y737991B");
// }

// @PostMapping(path = "/pay")
// @Operation(summary = "[USER] - Initiate payment PAYPAL")
// @PreAuthorize("hasAnyRole('USER')")
// public ResponseEntity<PaypalResponseDto> payment(@RequestBody
// PaypalRequestDto dto) {
// try {
// Payment payment = service.createPayment(dto, PAYPAL_CANCEL_API,
// PAYPAL_SUCCESS_API);

// for (Links link : payment.getLinks()) {
// if (link.getRel().equals("approval_url")) {
// PaypalResponseDto response = new PaypalResponseDto();
// response.setPayUrl(link.getHref());
// return ResponseEntity.ok(response);
// }
// }
// // email: sb-c9mcj26117776@personal.example.com
// // password: e@LbdH4n
// throw new BadRequestException(GLOBAL_EXCEPTION);
// } catch (PayPalRESTException e) {
// e.printStackTrace();
// throw new BadRequestException(e.getMessage());
// } catch (Exception e) {
// throw new BadRequestException(GLOBAL_EXCEPTION);
// }

// }

// @GetMapping(path = PAYPAL_CANCEL_URL)
// @Operation(summary = "[ANORNYMOUS] - Redirect from PAYPAL")
// public RedirectView cancelPay() {
// return new RedirectView(PAYMENT_CANCEL_CLIENT);
// }

// @GetMapping(path = PAYPAL_SUCCESS_URL)
// @Operation(summary = "[ANORNYMOUS] - Redirect from PAYPAL")
// public RedirectView successPay(@RequestParam("paymentId") String paymentId,
// @RequestParam("PayerID") String payerId) {
// try {
// Payment payment = service.executePayment(paymentId, payerId);
// System.out.println(payment.toJSON());
// boolean isApproved = payment.getState().equals("approved");
// service.updateOrderAndCreateEnroll(payment, isApproved);
// if (isApproved) {
// return new RedirectView(PAYMENT_SUCCESS_CLIENT);
// }
// return new RedirectView(PAYMENT_CANCEL_CLIENT);
// } catch (PayPalRESTException e) {
// throw new BadRequestException(e.getMessage());
// } catch (Exception e) {
// throw new BadRequestException(GLOBAL_EXCEPTION);
// }
// }
// }
