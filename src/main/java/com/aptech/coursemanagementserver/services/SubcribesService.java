package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.SubcribesDto;

public interface SubcribesService {
    public SubcribesDto findById(long id);

    public List<SubcribesDto> findByUserId(long userId);

    public List<SubcribesDto> findByAuthorId(long authorId);

    public void subcribe(SubcribesDto subcribesDto);

    public void unSubcribes(long authorId, long userId);
}
