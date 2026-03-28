package com.cts.edusphere.common.validation;

import jakarta.validation.groups.Default;

/**
 * Bean-validation group marker for constraints that apply only when an existing
 * resource is being <strong>updated</strong>.
 *
 * <p>Use this interface as the {@code groups} attribute on Jakarta Validation
 * constraints in request DTOs to indicate that the constraint is only enforced
 * during update operations.  Extending {@link Default} ensures that all
 * ungrouped constraints are still validated alongside update-specific ones.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * @NotNull(groups = OnUpdate.class, message = "Officer ID is required")
 * UUID officerId;
 * }</pre>
 */
public interface OnUpdate extends Default {
}
