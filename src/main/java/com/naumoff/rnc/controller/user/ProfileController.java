package com.naumoff.rnc.controller.user;

import com.naumoff.rnc.database.entities.post.PostEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.entities.users.UserProfileEntity;
import com.naumoff.rnc.database.repository.users.UserRepository;
import com.naumoff.rnc.dto.menu.MenuCollectionDto;
import com.naumoff.rnc.dto.users.ProfileForm;
import com.naumoff.rnc.dto.users.history.UserHistoryInterface;
import com.naumoff.rnc.model.AuthenticatedUser;
import com.naumoff.rnc.services.breadcrumbs.BreadcrumbsService;
import com.naumoff.rnc.services.cities.CityService;
import com.naumoff.rnc.services.menu.MainMenuService;
import com.naumoff.rnc.services.post.PostService;
import com.naumoff.rnc.services.users.UserHistoryService;
import com.naumoff.rnc.services.users.UserProfileService;
import com.naumoff.rnc.services.users.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;

@Controller
public class ProfileController {

    private final PostService postService;
    private final UserRepository userRepository;
    private final BreadcrumbsService breadcrumbsService;
    private final CityService cityService;
    private final UserProfileService userProfileService;
    private final UserHistoryService userHistoryService;
    private final UserService userService;
    private final MainMenuService mainMenuService;

    public ProfileController(
            PostService postService,
            UserRepository userRepository,
            BreadcrumbsService breadcrumbsService,
            CityService cityService,
            UserProfileService userProfileService,
            UserHistoryService userHistoryService,
            UserService userService,
            MainMenuService mainMenuService
    ) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.breadcrumbsService = breadcrumbsService;
        this.cityService = cityService;
        this.userProfileService = userProfileService;
        this.userHistoryService = userHistoryService;
        this.userService = userService;
        this.mainMenuService = mainMenuService;
    }

    @GetMapping("/profile")
    public String profile(
            Model model,
            @AuthenticationPrincipal
            AuthenticatedUser authUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (authUser == null) {
            return "redirect:/login";
        }

        UserEntity currentUser = authUser.getEntity();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("authUser", authUser);

        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> postEntityPage = postService.getUserPosts(currentUser, pageable);
        model.addAttribute("posts", postEntityPage.getContent());
        model.addAttribute("page", postEntityPage);

        MenuCollectionDto menuCollectionDto = mainMenuService.getMenuCollectionDto();
        mainMenuService.assignMenuToTemplate(model, menuCollectionDto);

        breadcrumbsService.assignBreadcrumbsToModel(
                breadcrumbsService.BR_PROFILE,
                model
        );

        return "user/profile";
    }

    @GetMapping("/user/profile/add")
    public String profileAdd(
            Model model,
            @AuthenticationPrincipal
            AuthenticatedUser authUser
    ) {
        UserEntity currentUser = authUser.getEntity();
        model.addAttribute("currentUser", currentUser);

        ProfileForm profileForm = new ProfileForm();
        model.addAttribute("profileForm", profileForm);      // DTO формы

        model.addAttribute("cities", cityService.getAllAvailableCity());                // список городов
        model.addAttribute("hasRoleEditor", false);  // boolean: можно ли менять роль
        model.addAttribute("errors", new HashMap<String, String>());                // Map<String, String> ошибок валидации
        model.addAttribute("todayDate", LocalDate.now().toString()); // для max в date

        model.addAttribute("currentProfile", userProfileService.getCurrentUserProfile(userService.getUserById(currentUser.getId())));

        MenuCollectionDto menuCollectionDto = mainMenuService.getMenuCollectionDto();
        mainMenuService.assignMenuToTemplate(model, menuCollectionDto);

        return "user/profile/add";
    }

    @PostMapping(value = "/user/profile/add")
    public String profileSave(
            @AuthenticationPrincipal
            AuthenticatedUser authUser,
            @ModelAttribute ProfileForm profileForm,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        UserEntity currentUser = authUser.getEntity();

        UserProfileEntity userProfileEntity = userProfileService.saveProfile(profileForm, currentUser);

        userHistoryService.createHistoryRecord(
                "Вы создали профаил",
                "Ваш профаил был успешно создан",
                currentUser,
                UserHistoryInterface.OBJECT_TYPE_PROFILE,
                userProfileEntity.getId()
        );

        // save city
        userService.changeCity(currentUser, profileForm.getCityId());

        return "redirect:/profile";
    }

    @GetMapping("/user/{userId}/profile")
    public String profile(
            @PathVariable("userId") Long userId,
            Model model,
            @AuthenticationPrincipal
            AuthenticatedUser authUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserEntity currentUser = this.userRepository.findById(userId).get();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("authUser", authUser);


        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> postEntityPage = postService.getUserPosts(currentUser, pageable);

        model.addAttribute("posts", postEntityPage.getContent());
        model.addAttribute("page", postEntityPage);

        breadcrumbsService.assignBreadcrumbsToModel(
                breadcrumbsService.BR_PROFILE,
                model
        );

        MenuCollectionDto menuCollectionDto = mainMenuService.getMenuCollectionDto();
        mainMenuService.assignMenuToTemplate(model, menuCollectionDto);

        return "user/profile";
    }

    @PostMapping("/profile/post")
    public String createPost(@AuthenticationPrincipal AuthenticatedUser authUser,
                             @RequestParam String content,
                             RedirectAttributes ra) {
        UserEntity user = authUser.getEntity();
        if (content != null && !content.isBlank()) {
            PostEntity postEntity = postService.createPost(user, content.trim());

            userHistoryService.createHistoryRecord(
                    "Вы создали запись в блоге",
                    "Вы успешно создали запись в своем блоге",
                    user,
                    UserHistoryInterface.OBJECT_TYPE_RECORD_POST,
                    postEntity.getId()
            );

            ra.addFlashAttribute("success", "Запись опубликована!");
        }
        return "redirect:/profile";
    }
}
