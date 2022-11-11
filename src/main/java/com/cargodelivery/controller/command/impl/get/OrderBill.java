package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.controller.command.CommandList;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.impl.OrderDaoImpl;
import com.cargodelivery.dao.impl.UserDaoImpl;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.service.AppUtils;
import com.cargodelivery.service.OrderService;
import com.cargodelivery.service.UserService;
import com.cargodelivery.service.impl.OrderServiceImpl;
import com.cargodelivery.service.impl.UserServiceImpl;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OrderBill implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(OrderBill.class);
    private final OrderService orderService;
    private final UserService userService;

    public OrderBill() {
        orderService = new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl());
        userService = new UserServiceImpl(new UserDaoImpl());
    }

    /**
     * Process code of one of the command from {@link CommandList}
     *
     * @param req  {@link HttpServletRequest}
     * @param resp {@link HttpServletResponse}
     * @return JSP url
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        int orderId = Integer.parseInt(AppUtils.checkReqParam(req, "orderId"));
        try {
            User user = userService.findUser((User) session.getAttribute("user"));
            Order order = orderService.findOrder(orderId);

            resp.setContentType("application/pdf");

            Document document = new Document();
            PdfWriter.getInstance(document, resp.getOutputStream());

            document.open();

            PdfPTable orderInfoTable = new PdfPTable(3);
            orderInfoTable.setWidthPercentage(100);
            orderInfoTable.addCell(getIRHCell(""));
            orderInfoTable.addCell(getIRHCell(""));
            orderInfoTable.addCell(getIRHCell("Invoice"));
            orderInfoTable.addCell(getIRHCell(""));
            orderInfoTable.addCell(getIRHCell(""));

            FontSelector fs = new FontSelector();
            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);
            fs.addFont(font);
            Phrase bill = fs.process("Bill To");
            Paragraph name = new Paragraph(user.getName() + " " + user.getSurname());
            name.setIndentationLeft(20);
            Paragraph contact = new Paragraph("EMAIL: " + user.getEmail());
            contact.setIndentationLeft(20);
            Paragraph role = new Paragraph("ROLE: " + user.getRole().toString());
            role.setIndentationLeft(20);

            PdfPTable billTable = new PdfPTable(6);
            billTable.setWidthPercentage(100);
            billTable.setWidths(new float[]{2, 3, 4, 3, 3, 2});
            billTable.setSpacingBefore(30.0f);
            billTable.addCell(getBillHeaderCell("Order Id"));
            billTable.addCell(getBillHeaderCell("Route"));
            billTable.addCell(getBillHeaderCell("Cargo details"));
            billTable.addCell(getBillHeaderCell("Registration date"));
            billTable.addCell(getBillHeaderCell("Delivery date"));
            billTable.addCell(getBillHeaderCell("Price, ₴"));

            billTable.addCell(getBillRowCell(String.valueOf(order.getId())));
            billTable.addCell(getBillRowCell(order.getRoute()));
            billTable.addCell(getBillRowCell(order.getCargo().toString()));
            billTable.addCell(getBillRowCell(order.getRegistrationDate().toString()));
            billTable.addCell(getBillRowCell(order.getDeliveryDate().toString()));
            billTable.addCell(getBillRowCell(order.getPrice().toString()));

            PdfPTable validity = new PdfPTable(1);
            validity.setWidthPercentage(100);
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell("Warranty"));
            validity.addCell(getValidityCell(" * Products purchased comes with 1 year national warranty \n   (if applicable)"));
            validity.addCell(getValidityCell(" * Warranty should be claimed only from the respective manufactures"));

            PdfPCell summaryL = new PdfPCell(validity);
            summaryL.setColspan(3);
            summaryL.setPadding(1.0f);
            billTable.addCell(summaryL);

            var discount = order.getPrice().doubleValue() * 0.1;
            var tax = order.getPrice().doubleValue() * 0.025;
            var total = order.getPrice().doubleValue() - discount + tax;

            PdfPTable accounts = new PdfPTable(2);
            accounts.setWidthPercentage(100);
            accounts.addCell(getAccountsCell("Subtotal"));
            accounts.addCell(getAccountsCellR(order.getPrice().toString()));
            accounts.addCell(getAccountsCell("Discount (10%)"));
            accounts.addCell(getAccountsCellR(String.valueOf(discount)));
            accounts.addCell(getAccountsCell("Tax(2.5%)"));
            accounts.addCell(getAccountsCellR(String.valueOf(tax)));
            accounts.addCell(getAccountsCell("Total"));
            accounts.addCell(getAccountsCellR(String.valueOf(total)));

            PdfPCell summaryR = new PdfPCell(accounts);
            summaryR.setColspan(3);
            billTable.addCell(summaryR);

            PdfPTable describer = new PdfPTable(1);
            describer.setWidthPercentage(100);
            describer.addCell(getDescCell(" "));
            describer.addCell(getDescCell("Goods once sold will not be taken back or exchanged || Subject to product justification || Product damage no one responsible || "
                    + " Service only at concerned authorized service centers"));

            document.add(bill);
            document.add(name);
            document.add(contact);
            document.add(role);
            document.add(billTable);
            document.add(describer);

            document.close();

            return "profile_user.jsp";
        } catch (OrderServiceException | UserServiceException | IOException | DocumentException e) {
            LOG.error(e.getMessage(), e);
            session.setAttribute("errorMessage", e.getMessage());
            return CommandList.ERROR_PAGE.getCommand().execute(req, resp);
        }
    }

    private PdfPCell getIRHCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private PdfPCell getBillHeaderCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5.0f);
        return cell;
    }

    private PdfPCell getBillRowCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        return cell;
    }

    private PdfPCell getValidityCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorder(0);
        return cell;
    }

    private PdfPCell getAccountsCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setPadding(5.0f);
        return cell;
    }

    private PdfPCell getAccountsCellR(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5.0f);
        cell.setPaddingRight(20.0f);
        return cell;
    }

    private PdfPCell getDescCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(0);
        return cell;
    }
}
