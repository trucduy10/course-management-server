package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.TagDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.models.Tag;

public interface TagService {
    public Tag findTagByName(String tagName);

    public List<TagDto> findAll();

    public BaseDto create(TagDto tagDto);

    public BaseDto update(TagDto tagDto);

    public BaseDto delete(long tagId);

    public boolean saveAll(List<TagDto> tagDtos);

    public TagDto findById(long tagId);
}
