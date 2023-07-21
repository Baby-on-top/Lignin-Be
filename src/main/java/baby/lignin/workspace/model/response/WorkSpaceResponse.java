package baby.lignin.workspace.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkSpaceResponse {
    private Long workspaceId;
    private Long createId;
    private String name;
    private String image;
}
