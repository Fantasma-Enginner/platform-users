/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.27).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.tsir.toll.settings.users.infrastructure.api;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.tsir.common.api.ApiMessage;
import org.tsir.toll.settings.users.domain.dto.ProfileDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-09-18T01:57:31.631Z[GMT]")
@Validated
public interface ProfilesApi {

	@Operation(summary = "Consultar perfiles", description = "Obtener la lista de perfiles de acuerdo a los filtros establecidos.", security = {
			@SecurityRequirement(name = "bearerAuth") }, tags = { "Perfiles" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProfileDTO.class)))),

			@ApiResponse(responseCode = "200", description = "Contenido con el codigo y mensaje del error. ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessage.class))) })
	@RequestMapping(value = "/profiles", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<List<ProfileDTO>> findProfiles(
			@Parameter(in = ParameterIn.QUERY, description = "Llaves de los criterios de búsqueda. Enumeración de las posibles llaves:  * `CODE` - Codigo del perfil.  * `NAME` - Nombre del perfil.  * `STATE` - Estado del perfil. ", schema = @Schema()) @Valid @RequestParam(value = "filter", required = false) Map<String, String> filter,
			@Parameter(in = ParameterIn.QUERY, description = "Parámetros de paginado. Enumeración de las posibles propiedades:  * `INDEX` - Número de página de resultados.  * `SIZE` - Tamaño de la página de resultados - Mínimo 5, Máximo 100.", schema = @Schema()) @Valid @RequestParam(value = "paging", required = false) Map<String, Integer> paging,
			@Parameter(in = ParameterIn.QUERY, description = "Parámetros de ordenamiento de resultados. Enumeración de las posibles propiedades:  * `FIELD` - Campo de ordenamiento de los resultados de acuerdo a las llaves anteriores.  * `ORDER` - Especificación del orden de resultados ASC-DESC.", schema = @Schema()) @Valid @RequestParam(value = "sorting", required = false) Map<String, String> sorting);

	@Operation(summary = "Consultar perfil por identificador", description = "Obtiene la información de un perfil de usuarios.", security = {
			@SecurityRequirement(name = "bearerAuth") }, tags = { "Perfiles" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileDTO.class))),

			@ApiResponse(responseCode = "200", description = "Contenido con el codigo y mensaje del error. ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessage.class))) })
	@RequestMapping(value = "/profiles/{code}", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<ProfileDTO> getProfile(
			@Parameter(in = ParameterIn.PATH, description = "Parametro de codigo generel de identificación de un recurso.", required = true, schema = @Schema()) @PathVariable("code") Long code);

	@Operation(summary = "Consultar permisos asignados", description = "Obtiene la lista de permisos garantizados al perfil de usuarios.", security = {
			@SecurityRequirement(name = "bearerAuth") }, tags = { "Perfiles" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Long.class)))),

			@ApiResponse(responseCode = "200", description = "Contenido con el codigo y mensaje del error. ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessage.class))) })
	@RequestMapping(value = "/profiles/{code}/resources", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<List<Long>> getProfileResources(
			@Parameter(in = ParameterIn.PATH, description = "Parametro de codigo generel de identificación de un recurso.", required = true, schema = @Schema()) @PathVariable("code") Long code);

	@Operation(summary = "Asignar permisos a perfil", description = "Realiza la asignación de permisos para acceso a los recursos espcecificados para el perfil indicado.", security = {
			@SecurityRequirement(name = "bearerAuth") }, tags = { "Perfiles" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Acceso a recursos asignado correctamente."),

			@ApiResponse(responseCode = "200", description = "Contenido con el codigo y mensaje del error. ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessage.class))) })
	@RequestMapping(value = "/profiles/{code}/resources", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	ResponseEntity<Void> grantResource(
			@Parameter(in = ParameterIn.PATH, description = "Parametro de codigo generel de identificación de un recurso.", required = true, schema = @Schema()) @PathVariable("code") Long code,
			@Parameter(in = ParameterIn.DEFAULT, description = "Lista de recusrsos a asignar para acceso al perfil", required = true, schema = @Schema()) @Valid @RequestBody List<Long> body);

	@Operation(summary = "Registrar perfil", description = "Solicitud de creación de un perfil en el sistema.", security = {
			@SecurityRequirement(name = "bearerAuth") }, tags = { "Perfiles" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Perfil registrado satisfactoriamente"),

			@ApiResponse(responseCode = "200", description = "Contenido con el codigo y mensaje del error. ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessage.class))) })
	@RequestMapping(value = "/profiles", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	ResponseEntity<Void> registerProfile(
			@Parameter(in = ParameterIn.DEFAULT, description = "Información requerida del perfil.", required = true, schema = @Schema()) @Valid @RequestBody ProfileDTO body);

	@Operation(summary = "Revocar permisos a perfil", description = "Elimina el acceso del perfil a los recursos especificados.", security = {
			@SecurityRequirement(name = "bearerAuth") }, tags = { "Perfiles" })
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Permiso revocado correctamente."),

			@ApiResponse(responseCode = "200", description = "Contenido con el codigo y mensaje del error. ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessage.class))) })
	@RequestMapping(value = "/profiles/{code}/resources/{resourceId}", produces = {
			"application/json" }, method = RequestMethod.DELETE)
	ResponseEntity<Void> revokeResource(
			@Parameter(in = ParameterIn.PATH, description = "Parametro de codigo generel de identificación de un recurso.", required = true, schema = @Schema()) @PathVariable("code") Long code,
			@Parameter(in = ParameterIn.PATH, description = "Parametro de identificador de recursos del sistema.", required = true, schema = @Schema()) @PathVariable("resourceId") Long resourceId);

	@Operation(summary = "Actualizar de información de perfil", description = "Solicitud de modificación de perfil.", security = {
			@SecurityRequirement(name = "bearerAuth") }, tags = { "Perfiles" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Recurso consultado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileDTO.class))),

			@ApiResponse(responseCode = "200", description = "Contenido con el codigo y mensaje del error. ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessage.class))) })
	@RequestMapping(value = "/profiles/{code}", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.PUT)
	ResponseEntity<ProfileDTO> updateProfile(
			@Parameter(in = ParameterIn.PATH, description = "Parametro de codigo generel de identificación de un recurso.", required = true, schema = @Schema()) @PathVariable("code") Long code,
			@Parameter(in = ParameterIn.DEFAULT, description = "Datos a modificar del perfil.", required = true, schema = @Schema()) @Valid @RequestBody ProfileDTO body);

}
