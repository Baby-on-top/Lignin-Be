package baby.lignin.workspace.service;


import baby.lignin.auth.config.TokenResolver;
import baby.lignin.workspace.entity.WorkSpaceEntitiy;
import baby.lignin.workspace.entity.WorkSpaceMemberEntity;
import baby.lignin.workspace.model.request.WorkSpaceCreateRequest;
import baby.lignin.workspace.model.request.WorkSpaceDeleteRequest;
import baby.lignin.workspace.model.request.WorkSpaceUpdateRequest;
import baby.lignin.workspace.model.response.WorkSpaceMemberResponse;
import baby.lignin.workspace.model.response.WorkSpaceResponse;
import baby.lignin.workspace.repository.WorkspaceMemberRepository;
import baby.lignin.workspace.repository.WorkspaceRepository;
import baby.lignin.workspace.support.converter.WorkspaceConverter;
import baby.lignin.workspace.support.converter.WorkspaceCreateConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WorkspaceServiceImpl implements WorkspaceService {


    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final TokenResolver tokenResolver;

    @Override
    public List<WorkSpaceResponse> findAllList() {
        List<WorkSpaceEntitiy> list = workspaceRepository.findAll();

        List<WorkSpaceResponse> responses = new ArrayList<>();
        for(WorkSpaceEntitiy entity : list){
            System.out.println("name: "+entity.getName());
            responses.add(WorkspaceConverter.from(entity));
        }
        return responses;
    }

    @Override
    public List<WorkSpaceResponse> findMyList(String access_Token) throws Exception {
        Optional<Long> memberIdRe = tokenResolver.resolveToken(access_Token);
        Long memberId = memberIdRe.get();
        List<WorkSpaceMemberEntity> list = workspaceMemberRepository.findByMemberId(memberId).stream().collect(Collectors.toList());
        List<WorkSpaceResponse> response = new ArrayList<>();
        for(WorkSpaceMemberEntity entity : list){
            WorkSpaceResponse respone = WorkspaceConverter.from(workspaceRepository.findById(entity.getWorkspaceId()).orElseThrow(()-> new Exception("에러입니다.")));
            response.add(respone);
        }

        return response;
    }

    @Override
    public WorkSpaceResponse create(String token,WorkSpaceCreateRequest request) {
        Optional<Long> memberIdRe = tokenResolver.resolveToken(token);
        Long memberId = memberIdRe.get();
        request.setCreateId(memberId);
        WorkSpaceEntitiy workSpaceEntitiy = workspaceRepository.save(WorkspaceConverter.to(request));
        workspaceMemberRepository.save(WorkspaceCreateConverter.to(workSpaceEntitiy));
        return WorkspaceConverter.from(workSpaceEntitiy);
    }

    @Override
    public WorkSpaceResponse delete(Long workspaceId) throws Exception {
        WorkSpaceEntitiy workSpaceEntitiy = workspaceRepository.findById(workspaceId).orElseThrow(()-> new Exception("찾는 워크 스페이스가 없습니다.!"));
        workspaceRepository.delete(workSpaceEntitiy);
        return WorkspaceConverter.from(workSpaceEntitiy);
    }

    @Override
    public WorkSpaceResponse update(WorkSpaceUpdateRequest request) throws Exception {
        WorkSpaceEntitiy workSpaceEntitiy = workspaceRepository.findById(request.getWorkspaceId()).orElseThrow(()-> new Exception("찾는 워크 스페이스가 없습니다.!"));
        workSpaceEntitiy.changeWorkSpaceInfo(request);
        workspaceRepository.save(workSpaceEntitiy);
        return WorkspaceConverter.from(workSpaceEntitiy);
    }

    @Override
    public WorkSpaceMemberResponse unlink(String token, WorkSpaceDeleteRequest request) {
        Optional<Long> memberIdRe = tokenResolver.resolveToken(token);
        Long memberId = memberIdRe.get();
        WorkSpaceMemberEntity workSpaceMemberEntity = workspaceMemberRepository.findByMemberIdAndWorkspaceId(memberId,request.getWorkspaceId());
        workspaceMemberRepository.delete(workSpaceMemberEntity);
        return WorkspaceConverter.from(workSpaceMemberEntity);
    }
}