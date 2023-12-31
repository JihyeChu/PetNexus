package com.sparta.petnexus.trade.controller;

import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.trade.dto.TradeResponseDto;
import com.sparta.petnexus.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequiredArgsConstructor
public class TradeViewController {

    private final TradeService tradeService;

    @GetMapping("/tradeMarket")
    public String tradeList(Model model, @RequestParam("page") Optional<Integer> page,
                            @RequestParam("size") Optional<Integer> size,
                            @RequestParam("sortBy") Optional<String> sortBy,
                            @RequestParam("isAsc") Optional<Boolean> isAsc) {
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        String sort = sortBy.orElse("id");
        boolean Asc = isAsc.orElse(true);
        Page<TradeResponseDto> tradeResponseDtoList = tradeService.getTrade(currentPage, pageSize, sort, Asc);
        model.addAttribute("tradeList", tradeResponseDtoList);
        int totalPages = tradeResponseDtoList.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "tradeMarket";
    }

    @GetMapping("/tradeMarket/keyword/{keyword}")
    public String tradeKeywordList(Model model, @PathVariable String keyword, @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<TradeResponseDto> searchList = tradeService.searchTrade(keyword, pageable);
        model.addAttribute("tradeList", searchList);
        int totalPages = searchList.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "tradeMarket";
    }

    @GetMapping("/tradeMarket/{tradeId}")
    public String getTrade(@PathVariable Long tradeId, Model model) {
        TradeResponseDto tradeResponseDto = tradeService.selectTrade(tradeId);
        model.addAttribute("trade", tradeResponseDto);
        return "trade";
    }

    @GetMapping("/tradeMarket/trade")
    public String createTrade(@RequestParam(required = false) Long tradeId, Model model, @AuthenticationPrincipal
    UserDetailsImpl userDetails, RedirectAttributes rttr) {
        if (userDetails == null) {
            rttr.addFlashAttribute("result", "로그인이 필요합니다.");
            return "redirect:/tradeMarket";
        } else {
            if (tradeId == null) {
                model.addAttribute("trade", new TradeResponseDto());
            } else {
                TradeResponseDto tradeResponseDto = tradeService.selectTrade(tradeId);
                model.addAttribute("trade", tradeResponseDto);
            }
            return "createTrade";
        }
    }

    @GetMapping("/mytradeMarket")
    public String mytradeList(Model model, @RequestParam("page") Optional<Integer> page,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("sortBy") Optional<String> sortBy,
            @RequestParam("isAsc") Optional<Boolean> isAsc) {
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        String sort = sortBy.orElse("id");
        boolean Asc = isAsc.orElse(true);
        Page<TradeResponseDto> tradeResponseDtoList = tradeService.getmyTrade(currentPage, pageSize, sort, Asc, userDetails);
        model.addAttribute("tradeList", tradeResponseDtoList);
        int totalPages = tradeResponseDtoList.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "mytradeMarket";
    }

    @GetMapping("/mytradeMarket/{tradeId}")
    public String getmyTrade(@PathVariable Long tradeId, Model model) {
        TradeResponseDto tradeResponseDto = tradeService.selectTrade(tradeId);
        model.addAttribute("trade", tradeResponseDto);
        return "mytrade";
    }
}