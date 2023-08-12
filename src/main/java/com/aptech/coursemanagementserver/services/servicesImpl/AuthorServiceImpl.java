package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.AuthorDto;
import com.aptech.coursemanagementserver.dtos.AuthorInterface;
import com.aptech.coursemanagementserver.dtos.AuthorRequestDto;
import com.aptech.coursemanagementserver.enums.Role;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.IsExistedException;
import com.aptech.coursemanagementserver.models.Author;
import com.aptech.coursemanagementserver.models.Enrollment;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.repositories.AuthorRepository;
import com.aptech.coursemanagementserver.services.AuthorService;
import com.aptech.coursemanagementserver.services.authServices.UserService;
import com.aptech.coursemanagementserver.specification.AuthorSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final UserService userService;

    @Override
    public AuthorDto findById(long id) {
        Author author = authorRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(
                        "This author with authorId: [" + id + "] is not exist."));

        return toDto(author);
    }

    @Override
    public List<AuthorDto> findAll() {
        List<Author> authors = authorRepository.findAll();

        List<AuthorDto> authorDtos = new ArrayList<>();

        for (Author author : authors) {
            AuthorDto authorDto = toDto(author);
            authorDtos.add(authorDto);
        }

        return authorDtos;
    }

    @Override
    public List<AuthorInterface> findTop3() {
        List<AuthorInterface> findTop3 = authorRepository.findTop3();
        return findTop3;
    }

    @Override
    public Page<AuthorDto> findAllPagination(AuthorRequestDto requestDto) {

        Pageable pageable = PageRequest.of(requestDto.getPageNo(), requestDto.getPageSize(),
                Sort.by("createdAt"));

        if (requestDto.getSortKey().equalsIgnoreCase("DESC")) {
            pageable = PageRequest.of(requestDto.getPageNo(), requestDto.getPageSize(),
                    Sort.by("createdAt").descending());
        }

        Page<Author> authors = authorRepository.findAll(AuthorSpecification.hasCategoryAndName(requestDto), pageable);
        List<AuthorDto> authorDtos = new ArrayList<>();
        for (Author author : authors.getContent()) {
            AuthorDto authorDto = toDto(author);
            authorDtos.add(authorDto);
        }
        Page<AuthorDto> pageAuthorDtos = new PageImpl<>(authorDtos, authors.getPageable(), authors.getTotalElements());
        return pageAuthorDtos;
    }

    // @Override
    // public Page<AuthorDto> findAllPaginationFilter(int pageNo, int pageSize, long
    // categoryId) {
    // Pageable pageable = PageRequest.of(pageNo, pageSize,
    // Sort.by("created_at").descending());
    // Page<Author> authors = authorRepository.findAllPaginationFilter(categoryId,
    // pageable);

    // List<AuthorDto> authorDtos = new ArrayList<>();
    // for (Author author : authors.getContent()) {
    // AuthorDto authorDto = toDto(author);
    // authorDtos.add(authorDto);
    // }
    // Page<AuthorDto> pageAuthorDtos = new PageImpl<>(authorDtos,
    // authors.getPageable(), authors.getTotalElements());
    // return pageAuthorDtos;
    // }

    @Override
    public void save(AuthorDto authorDto) {
        User user = userService.findCurrentUser();
        Author author = new Author();
        if (authorDto.getId() > 0) {
            author = authorRepository.findById(authorDto.getId()).orElseThrow(
                    () -> new NoSuchElementException(
                            "This author with authorId: [" + authorDto.getId() + "] is not exist."));
        }
        if (authorDto.getId() == 0
                && findAll().stream().map(authorr -> authorr.getName()).toList().contains(authorDto.getName())) {
            throw new IsExistedException(authorDto.getName());
        }
        author.setName(authorDto.getName());
        author.setImage(authorDto.getImage());
        author.setTitle(authorDto.getTitle());
        author.setInformation(authorDto.getInformation());
        author.setUpdatedBy(user.getEmail().split("@")[0]);
        authorRepository.save(author);
    }

    @Override
    public void saveAll(List<AuthorDto> authorDtos) {
        List<Author> authors = new ArrayList<>();

        for (AuthorDto authorDto : authorDtos) {
            Author author = new Author();
            author.setName(authorDto.getName());
            author.setImage(authorDto.getImage());
            author.setInformation(authorDto.getInformation());
            author.setTitle(authorDto.getTitle());
            authors.add(author);
        }
        authorRepository.saveAll(authors);
    }

    @Override
    public void deleteAuthor(long authorId) {
        Author author = authorRepository.findById(authorId).orElseThrow(
                () -> new NoSuchElementException("This author with authorId: [" + authorId + "] is not exist."));
        author.getCourses().forEach(c -> {
            Stream<Enrollment> filter = c.getEnrollments().stream().filter(e -> e.getUser().getRole() == Role.USER);
            if (filter.count() > 0) {
                throw new BadRequestException("Cannot delete author who has already taught courses.");
            }
        });

        authorRepository.delete(author);
    }

    private AuthorDto toDto(Author author) {
        AuthorDto authorDto = AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .image(author.getImage())
                .title(author.getTitle())
                .information(author.getInformation())
                .created_at(author.getCreatedAt())
                .updatedBy(author.getUpdatedBy())
                .build();
        return authorDto;
    }

}
