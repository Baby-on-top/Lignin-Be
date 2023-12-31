package baby.lignin.workspace.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkspaceResponse {
    private Long workspaceId;
    private Long createId;
    private String workspaceName;
    private String workspaceImage;
}
