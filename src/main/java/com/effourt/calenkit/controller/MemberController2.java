package com.effourt.calenkit.controller;

import com.effourt.calenkit.domain.Member;
import com.effourt.calenkit.exception.MemberNotFoundException;
import com.effourt.calenkit.repository.MemberRepository;
import com.effourt.calenkit.service.AdminService;
import com.effourt.calenkit.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController2 {
    private final MyPageService myPageService;
    private final AdminService adminService;
    private final MemberRepository memberRepository;

    /** 관리자 */
    // Admin
    @GetMapping(value = "/admin")
    public String AdminList() {
        return "member/admin";
    }


    // Admin
    // 전체 멤버 리스트 출력 & 멤버 아이디 검색 후 리스트 출력(GET)
    // 전체 리스트 출력 시 Json형식의 text로 List객체 전달
    @GetMapping(value = "/admin_memberList")
    @ResponseBody
    public List<Member> AdminIdList(@RequestParam(required = false) String keyword) {
        List<Member> memberList = memberRepository.findAllByMemId(keyword);
        return memberList;
    }


    // Admin
    // 멤버 상태변경(PATCH)
    //  member_list 페이지로 이동
    @PatchMapping(value ="/admin_statusModify")
    public String AdminModify(@ModelAttribute Member member) throws MemberNotFoundException {
        Member selectMember=memberRepository.findByMemId(member.getMemId());
        return "adminPage";
    }

    // Admin
    // 멤버 삭제(DELETE)
    // 로그인세션에서 아이디값을 전달받아 member_list 페이지로 이동
    @DeleteMapping(value ="/admin_delete")
    public String AdminDelete(@ModelAttribute Member member) throws MemberNotFoundException {
        adminService.removeMember(member);
        return "adminPage";
    }

    /** 마이 페이지 */

    //MyPage 이동
    @GetMapping(value = "/myPage")
    public String MyPage() {
        return "member/myPage";
    }
    @GetMapping(value = "/myPage_delete")
    public String MyPageDelete() {
        return "member/myPageDelete";
    }
    @GetMapping(value = "/myPage_pwModify")
    public String MyPageModify() {
        return "member/myPageModify";
    }
//    //파일 upload 기본 틀
//    @PostMapping("/upload")
//    public String upload(@RequestParam("file") MultipartFile file, Model model, @ModelAttribute Member member) throws IOException {
//        if (file.isEmpty()) {
//            return "member/upload_fail";
//        }
//
//        String originalFilename = file.getOriginalFilename();
//        String extension = FilenameUtils.getExtension(originalFilename);
//        String uploadFilename = FilenameUtils.getBaseName(originalFilename) + "_" + System.currentTimeMillis() + "." + extension;
//        String uploadDirectory = "/resources/images/member/";
//
//        Path uploadPath = Paths.get(uploadDirectory);
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//
//        Path filePath = uploadPath.resolve(uploadFilename);
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//        model.addAttribute("originalFilename", originalFilename);
//        model.addAttribute("uploadFilename", uploadFilename);
//
//        return "";
//    }




    // MyPage
    // 멤버 닉네임 검색 후 중복 확인(GET)
    // Ajax 처리를 위해 닉네임 중복 갯수 반환
    @GetMapping("/nameCheck")
    @ResponseBody
    public int nameCheck(@RequestParam("memName") String memName) {
        int cnt = memberRepository.findByMemName(memName);
        return cnt;

    }
    // MyPage
    // 아이디 검색 후 중복 확인(GET)
    // Ajax 처리를 위해 아이디 중복 갯수 반환
//    @GetMapping("/memIdCheck")
//    @ResponseBody
//    public int idCheck(@RequestParam("memId") String memId) {
//        int cnt=0;
//        Member member = memberRepository.findByMemId(memId);
//        if(member.getMemId()==memId){
//            cnt++;
//            return cnt;
//        }
//        return cnt;
//    }

    // MyPage
    // 멤버 정보변경(GET)
    // 로그인세션에서 아이디값을 전달받아 member_modify 페이지로 이동
    @GetMapping(value ="/myPage_modify")
    public String MyPageModify(Model model, HttpSession session) throws MemberNotFoundException {
        Member member=(Member)session.getAttribute("loginMember");
        model.addAttribute("member", memberRepository.findByMemId(member.getMemId()));
        return "myPage";
    }


    // MyPage
    // 멤버 정보변경(PATCH)
    // Form태그를 통해 전달받은 값들을 member 객체에 update 후 member_modify 페이지로 이동
    @PatchMapping(value ="/myPage_modify")
    public String MyPageModify(HttpSession session) throws MemberNotFoundException {
        Member member=(Member)session.getAttribute("loginMember");
        myPageService.modifyMe(member);

        return "redirect:myPage";
    }


//    // MyPage
//    // 멤버 비밀번호 정보변경(Put)
//    // 로그인세션에서 아이디값을 전달받아 member_pwModify 페이지로 이동처리.
//    @PutMapping(value ="/myPage_pwModify")
//    public String MyPagePwModify(HttpSession session) throws MemberNotFoundException {
//        Member member=(Member)session.getAttribute("loginMember");
//        myPageService.modifyPassword(member);
//        return "myPage";
//    }
//    // MyPage
//    // 멤버 상태 변경(Put)
//    // 로그인세션에서 아이디값을 전달받아 member_delete 페이지로 이동처리.
//    @PutMapping(value ="/myPage_delete")
//    public String MyPageDelete(HttpSession session) throws MemberNotFoundException {
//        Member member=(Member)session.getAttribute("loginMember");
//        myPageService.removeMe(member);
//        return "myPage";
//    }
}
