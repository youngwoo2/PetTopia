package com.sh.pettopia.Hojji.user.admin.controller;

import com.sh.pettopia.Hojji.user.member.dto.MemberListResponseDto;
import com.sh.pettopia.Hojji.user.member.entity.Authority;
import com.sh.pettopia.Hojji.user.member.entity.SitterStatus;
import com.sh.pettopia.Hojji.user.member.service.MemberService;
import com.sh.pettopia.choipetsitter.entity.PetSitter;
import com.sh.pettopia.choipetsitter.service.PetSitterService;
import com.sh.pettopia.common.paging.PageCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;
    private final PetSitterService petSitterService;


    @GetMapping("/dashboard")
    public void adminDashBoard() {
    }

//    @GetMapping("/memberList")
//    public String memberList(@ModelAttribute MemberListResponseDto memberResponseDto, Model model){
//        // 회원 전체 리스트
//        List<MemberListResponseDto> members = memberService.findAll();
//        log.debug("회원 전체목록을 불러옵니다. members = {}", members);
//        model.addAttribute("members", members);
//
//
//
//        return "admin/memberList";
//    }

    @GetMapping("/memberList")
    public void memberList(@PageableDefault(page = 1, size = 10) Pageable pageable,
                           @RequestParam(required = false) Authority authority,
                           @ModelAttribute MemberListResponseDto memberResponseDto, Model model){

        pageable = PageRequest.of(
                pageable.getPageNumber() -1,
                pageable.getPageSize());

        // 회원 전체 리스트
        Page<MemberListResponseDto> members = memberService.findAll(authority,pageable);
        log.debug("회원 전체목록을 불러옵니다. members = {}", members);
        model.addAttribute("members", members);


        // 페이징바 영역
        int page = members.getNumber(); // 0 - based 페이지 번호
        int limit = members.getSize();
        int totalCount = (int) members.getTotalElements();

        String url = "memberList"; // 상대주소
        if (authority != null) {
            url += "authority?=" + authority;
        }
        model.addAttribute("pageCriteria", new PageCriteria(page, limit, totalCount, url));
        log.debug("pageCriteria = {}", new PageCriteria(page, limit, totalCount, url));

        log.debug("url = {}", url);
    }


    @GetMapping("/sitterList")
    public String sitterList(@ModelAttribute MemberListResponseDto memberListResponseDto, Model model){
        // 시터 회원 전체 리스트
        List<MemberListResponseDto> sitters = memberService.findAllSitterMembers();
        log.debug("시터인 회원목록을 불러옵니다");
        log.debug("sitters = {}", sitters);
        model.addAttribute("sitters", sitters);

        return "admin/sitterList";
    }

    @GetMapping("/sitterRequestList")
    public String sitterRequestList(@ModelAttribute MemberListResponseDto memberResponseDto, Model model){
        // 시터 요청 대기중(pending) 상태의 회원 불러오기
        List<MemberListResponseDto> pendingMembers = memberService.findPendingSitterMembers();
        log.debug("시터 자격을 요청한 회원을 불러옵니다.");
        log.debug("pendingMembers = {}", pendingMembers);
        model.addAttribute("pendingMembers", pendingMembers);

        return "admin/sitterRequestList";
    }

    @PostMapping("/updateSitterStatus/{memberId}")
    @ResponseBody
    public ResponseEntity<Void> updateSitterStatus(@PathVariable Long memberId, @RequestBody Map<String, String> result) {
        log.info("POST updateSitterStatus/{}",memberId);
        String status = result.get("status");
        // status문자열을 SitterStatus enum값으로 변환
        SitterStatus sitterStatus = SitterStatus.valueOf(status);
        MemberListResponseDto MemberDto=memberService.findById(memberId); // 펫시터 등록은 위한 멤버를 불러온다

        if (sitterStatus == SitterStatus.APPROVED) {
            // 시터권한 요청 승인 시, member의 sitterStauts를 PENDING(승인대기중) -> APPROVED(승인됨)
            memberService.updateSitterStatus(memberId, sitterStatus);
            // 수락된 회원에 한해 member에 role_sitter 권한 추가
            memberService.grantSitterAuthority(memberId);
            // 펫시터 권한을 가진 회원 목록에 추가
            PetSitter petSitter= petSitterService.saveMemberToEntity(MemberDto);

        } else if(sitterStatus == SitterStatus.NONE){
            // 시터권한 거절 시, member의 sitterStatus를 PENDING(승인대기중) -> NONE(신청 내역 없음)
            memberService.updateSitterStatus(memberId, sitterStatus);
        }

        return ResponseEntity.ok().build();
    }

//    public paging ()
}
