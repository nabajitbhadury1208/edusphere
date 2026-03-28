package com.cts.edusphere.common.validation;

import jakarta.validation.groups.Default;

/**
 * Bean-validation group marker for constraints that apply only when a new
 * resource is being <strong>created</strong>.
 *
 * <p>Use this interface as the {@code groups} attribute on Jakarta Validation
 * constraints in request DTOs to indicate that the constraint is only enforced
 * during creation operations.  Extending {@link Default} ensures that all
 * ungrouped constraints are still validated alongside create-specific ones.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * @NotBlank(groups = OnCreate.class, message = "Name cannot be blank")
 * String name;
 * }</pre>
 */
public interface OnCreate extends Default {
}
