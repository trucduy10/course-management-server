package com.aptech.coursemanagementserver.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.aptech.coursemanagementserver.dtos.AuthorDto;
import com.aptech.coursemanagementserver.dtos.AuthorInterface;
import com.aptech.coursemanagementserver.dtos.AuthorRequestDto;

public interface AuthorService {

    public AuthorDto findById(long id);

    public List<AuthorDto> findAll();

    public Page<AuthorDto> findAllPagination(AuthorRequestDto requestDto);

    // public Page<AuthorDto> findAllPaginationFilter(int pageNo, int pageSize, long
    // categoryId);

    public List<AuthorInterface> findTop3();

    public void save(AuthorDto authorDto);

    public void saveAll(List<AuthorDto> authorDtos);

    public void deleteAuthor(long authorId);
}
