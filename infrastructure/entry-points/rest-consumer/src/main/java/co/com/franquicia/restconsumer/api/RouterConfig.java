package co.com.franquicia.restconsumer.api;

import co.com.franquicia.restconsumer.dto.request.BranchRequest;
import co.com.franquicia.restconsumer.dto.request.FranchiseRequest;
import co.com.franquicia.restconsumer.dto.request.ProductRequest;
import co.com.franquicia.restconsumer.dto.response.ApiResponseDto;
import co.com.franquicia.restconsumer.dto.response.ErrorResponse;
import co.com.franquicia.restconsumer.handler.BranchHandler;
import co.com.franquicia.restconsumer.handler.FranchiseHandler;
import co.com.franquicia.restconsumer.handler.ProductHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class RouterConfig {

    private static final String API_V1 = "/api/v1";

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/franchises/create",
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "create",
                    operation = @Operation(
                            operationId = "createFranchise",
                            tags = {"Franquicias"},
                            summary = "Crear franquicia",
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Datos de la franquicia a crear",
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = FranchiseRequest.class),
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Franquicia Colombia",
                                                            value = """
                            {
                              "nombre_franquicia": "Franquicia Colombia"
                            }
                            """
                                                    ),
                                                    @ExampleObject(
                                                            name = "Franquicia Internacional",
                                                            value = """
                            {
                              "nombre_franquicia": "Franquicia Global Tech 2024"
                            }
                            """
                                                    )
                                            }
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Franquicia creada exitosamente",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 200,
                              "message": "La franquicia se creó exitosamente.",
                              "data": {
                                "id": 1,
                                "name": "Franquicia Colombia"
                              }
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación o nombre duplicado",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Nombre duplicado",
                                                                    value = """
                                {
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "La franquicia con nombre 'Franquicia Colombia' ya existe.",
                                  "timestamp": "2025-12-18T10:30:00",
                                  "path": "/api/v1/franchises/create"
                                }
                                """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Nombre vacío",
                                                                    value = """
                                {
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "l campo nombre_franquicia es obligatorio y no puede estar vacío ni ser nulo",
                                  "timestamp": "2025-12-18T10:30:00",
                                  "path": "/api/v1/franchises/create"
                                }
                                """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Not Found",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 404,
                              "error": "Not Found",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/franchises/creat"
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "message": "Error al conectar con la base de datos",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/franchises/create"
                            }
                            """
                                                    )
                                            )
                                    )
                            }
                    )


            ),
            @RouterOperation(
                    path = "/api/v1/franchises/{id}/name",
                    method = RequestMethod.PUT,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "updateName",
                    operation = @Operation(
                            operationId = "updateFranchiseName",
                            tags = {"Franquicias"},
                            summary = "Actualizar nombre de franquicia",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "ID de la franquicia")},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Nuevo nombre de la franquicia",
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = FranchiseRequest.class),
                                            examples = @ExampleObject(
                                                    value = """
                        {
                          "name": "Franquicia Colombia Premium"
                        }
                        """
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "La franquicia se Actualizo exitosamente.",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 200,
                              "message": "La franquicia se Actualizo exitosamente.",
                              "data": {
                                "id": 1,
                                "name": "Franquicia Colombia Premium"
                              }
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Franquicia no encontrada o nombre duplicado",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Franquicia no encontrada",
                                                                    value = """
                                {
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "Franquicia no encontrada con el id: 20",
                                  "timestamp": "2025-12-18T10:30:00",
                                  "path": "/api/v1/franchises/20/name"
                                }
                                """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Nombre duplicado",
                                                                    value = """
                                {
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "El nombre de la franquicia 'Franquicia Colombia Premium' ya está en uso",
                                  "timestamp": "2025-12-18T10:30:00",
                                  "path": "/api/v1/franchises/1/name"
                                }
                                """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Nombre igual al actual",
                                                                    value = """
                                {
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "El nombre de la franquicia 'Franquicia Colombia Premium' ya esta asignado a esta franquicia",
                                  "timestamp": "2025-12-18T10:30:00",
                                  "path": "/api/v1/franchises/1/name"
                                }
                                """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "La Ruta no fue encontrada/no existe.",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 404,
                              "error": "Not Found",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/franchises/1/nam"
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "message": "Error al conectar con la base de datos",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/franchises/1/name"
                            }
                            """
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/franchises",
                    method = RequestMethod.GET,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "getAll",
                    operation = @Operation(
                            operationId = "getAllFranchises",
                            tags = {"Franquicias"},
                            summary = "Listar todas las franquicias",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Lista de franquicias obtenida exitosamente",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Lista con datos",
                                                                    value = """
                                {
                                  "status": 200,
                                  "message": "Franquicias obtenidas exitosamente",
                                  "data": [
                                    {
                                      "id": 1,
                                      "name": "Franquicia Colombia"
                                    },
                                    {
                                      "id": 2,
                                      "name": "Franquicia Ecuador"
                                    }
                                  ]
                                }
                                """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Lista vacía",
                                                                    value = """
                                {
                                  "status": 200,
                                  "message": "No hay franquicias registradas",
                                  "data": []
                                }
                                """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "La Ruta no fue encontrada/no existe.",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 404,
                              "error": "Not Found",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/franchise"
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "message": "Error al conectar con la base de datos",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/franchises"
                            }
                            """
                                                    )
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler) {
        return RouterFunctions
                .route(POST(API_V1 + "/franchises/create").and(accept(APPLICATION_JSON)),
                        handler::create)
                .andRoute(PUT(API_V1 + "/franchises/{id}/name").and(accept(APPLICATION_JSON)),
                        handler::updateName)
                .andRoute(GET(API_V1 + "/franchises"),
                        handler::getAll);
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/branches/create",
                    method = RequestMethod.POST,
                    beanClass = BranchHandler.class,
                    beanMethod = "create",
                    operation = @Operation(
                            operationId = "createBranch",
                            tags = {"Sucursales"},
                            summary = "Crear sucursal",
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Datos de la sucursal a crear",
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = BranchRequest.class),
                                            examples = @ExampleObject(
                                                    value = """
                        {
                          "franchiseId": 1,
                          "name": "Sucursal Bogotá Norte"
                        }
                        """
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Sucursal creada exitosamente",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 200,
                              "message": "La Sucursal se creó exitosamente.",
                              "data": {
                                "id": 1,
                                "franchiseId": 1,
                                "name": "Sucursal Bogotá Norte"
                              }
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Franquicia no encontrada o validación fallida",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = { @ExampleObject(
                                                            name = "Franquicia no encontrada",
                                                            value = """
                            {
                              "status": 400,
                              "error": "Bad Request",
                              "message": "Franquicia no encontrada con el id: 999",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/branches/create"
                            }
                            """
                                                    ),
                                                    @ExampleObject(
                                                            name = "Nombre vacío",
                                                            value = """
                            {
                              "status": 400,
                              "error": "Bad Request",
                              "message": "El campo nombre_sucursal es obligatorio y no puede estar vacío ni ser nulo",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/branches/create"
                            }
                            """
                                                    ),
                                                            @ExampleObject(
                                                                    name = "Nombre duplicado",
                                                                    value = """
                            {
                              "status": 400,
                              "error": "Bad Request",
                              "message": "Ya existe una sucursal con el nombre: Sucursal Bogotá",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/branches/create"
                            }
                            """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "La Ruta no fue encontrada/no existe.",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 404,
                              "error": "Not Found",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/branches/creat"
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "message": "Error al conectar con la base de datos",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/branches/create"
                            }
                            """
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/branches/{id}/name",
                    method = RequestMethod.PUT,
                    beanClass = BranchHandler.class,
                    beanMethod = "updateName",
                    operation = @Operation(
                            operationId = "updateBranchName",
                            tags = {"Sucursales"},
                            summary = "Actualizar nombre de sucursal",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id")},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Nombre actualizado exitosamente",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 200,
                              "message": "Nombre de sucursal actualizado exitosamente",
                              "data": {
                                "id": 1,
                                "franchiseId": 1,
                                "name": "Sucursal Bogotá Centro"
                              }
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Sucursal no encontrada",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples ={ @ExampleObject(
                                                            name = "Sucursal no encontrada",
                                                            value = """
                            {
                              "status": 400,
                              "error": "Bad Request",
                              "message": "Sucursal no encontrada con el id: 999",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/branches/999/name"
                            }
                            """
                                                    ),
                                                            @ExampleObject(
                                                                    name = "Nombre vacío",
                                                                    value = """
                            {
                              "status": 400,
                              "error": "Bad Request",
                              "message": "El campo nombre_sucursal es obligatorio y no puede estar vacío ni ser nulo",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/branches/999/name"
                            }
                            """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Nombre Duplicado",
                                                                    value = """
                            {
                              "status": 400,
                              "error": "Bad Request",
                              "message": "El nombre de la Sucursal 'Sucursal Bogotá ' ya está en uso",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/branches/999/name"
                            }
                            """
                                                            ),@ExampleObject(
                                                            name = "Nombre igual al actual",
                                                            value = """
                            {
                              "status": 400,
                              "error": "Bad Request",
                              "message": "El nombre de la Sucursal 'Sucursal Bogotá ' ya esta asignado a esta Sucursal",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/branches/999/name"
                            }
                            """
                                                    )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "La Ruta no fue encontrada/no existe.",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 404,
                              "error": "Not Found",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "//api/v1/branches/4/nam"
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "message": "Error al conectar con la base de datos",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/branches/4/name"
                            }
                            """
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/franchises/{franchiseId}/branches",
                    method = RequestMethod.GET,
                    beanClass = BranchHandler.class,
                    beanMethod = "getByFranchise",
                    operation = @Operation(
                            operationId = "getBranchesByFranchise",
                            tags = {"Sucursales"},
                            summary = "Listar sucursales por franquicia",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "franchiseId")},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Sucursales obtenidas exitosamente.",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Lista con datos",
                                                                    value = """
                                {
                                  "status": 200,
                                  "message": "Sucursales obtenidas exitosamente.",
                                  "data": [
                                    {
                                        "id": 4,
                                        "franchiseId": 1,
                                        "name": "Sucursal Bogotá Norte"
                                    },
                                    {
                                        "id": 5,
                                        "franchiseId": 1,
                                        "name": "Sucursal Centro"
                                    },
                                    {
                                        "id": 6,
                                        "franchiseId": 1,
                                        "name": "Sucursal Bogotá Centro"
                                    }
                                  ]
                                }
                                """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Lista vacía",
                                                                    value = """
                                {
                                  "status": 200,
                                  "message": "Sucursales obtenidas exitosamente.",
                                  "data": []
                                }
                                """
                                                            )
                                                    }
                                            )
                                    ),@ApiResponse(
                                    responseCode = "400",
                                    description = "Franquicia no encontrada",
                                    content = @Content(
                                            schema = @Schema(implementation = ErrorResponse.class),
                                            examples = @ExampleObject(
                                                    name = "Franquicia no encontrada",
                                                    value = """
                            {
                              "status": 400,
                              "error": "Bad Request",
                              "message": "Franquicia no encontrada con este id: 999",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/franchises/999/branches"
                            }
                            """
                                            )
                                    )
                            )
                                    ,
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "La Ruta no fue encontrada/no existe.",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 404,
                              "error": "Not Found",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/franchises/1/branche"
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "message": "Error al conectar con la base de datos",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/franchises/5/branches"
                            }
                            """
                                                    )
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> branchRoutes(BranchHandler handler) {
        return RouterFunctions
                .route(POST(API_V1 + "/branches/create").and(accept(APPLICATION_JSON)),
                        handler::create)
                .andRoute(PUT(API_V1 + "/branches/{id}/name").and(accept(APPLICATION_JSON)),
                        handler::updateName)
                .andRoute(GET(API_V1 + "/franchises/{franchiseId}/branches"),
                        handler::getByFranchise);
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/products/create",
                    method = RequestMethod.POST,
                    beanClass = ProductHandler.class,
                    beanMethod = "create",
                    operation = @Operation(
                            operationId = "createProduct",
                            tags = {"Productos"},
                            summary = "Crear producto",requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Datos del producto a crear",
                            required = true,
                            content = @Content(
                                    schema = @Schema(implementation = ProductRequest.class),
                                    examples = @ExampleObject(
                                            value = """
                        {
                          "sucursal_id": 4,
                          "nombre_producto": "AirPods Pro 2",
                          "stock_producto": 200
                        }
                        """
                                    )
                            )
                    ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Producto creado exitosamente",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 200,
                              "message": "El producto se creó exitosamente.",
                              "data": {
                                "id": 10,
                                "branchId": 4,
                                "name": "AirPods Pro 2",
                                "stock": 200
                              }
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Validación fallida o sucursal no encontrada",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Sucursal no encontrada",
                                                                    value = """
                                {
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "Sucursal no encontrada con el id: 999",
                                  "timestamp": "2025-12-18T10:30:00",
                                  "path": "/api/v1/products/create"
                                }
                                """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Stock negativo",
                                                                    value = """
                                {
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "El stock no puede ser negativo",
                                  "timestamp": "2025-12-18T10:30:00",
                                  "path": "/api/v1/products/create"
                                }
                                """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "La Ruta no fue encontrada/no existe.",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 404,
                              "error": "Not Found",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/products/creat"
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "message": "Error al conectar con la base de datos",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/products/create"
                            }
                            """
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/products/{id}/stock",
                    method = RequestMethod.PUT,
                    beanClass = ProductHandler.class,
                    beanMethod = "updateStock",
                    operation = @Operation(
                            operationId = "updateProductStock",
                            tags = {"Productos"},
                            summary = "Actualizar stock de producto",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del producto")},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Nuevo stock del producto",
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = ProductRequest.class),
                                            examples = @ExampleObject(
                                                    value = """
                        {
                          "stock_producto": 180
                        }
                        """
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Stock actualizado exitosamente",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 200,
                              "message": "Stock actualizado exitosamente.",
                              "data": {
                                "id": 10,
                                "branchId": 4,
                                "name": "AirPods Pro 2",
                                "stock": 180
                              }
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Producto no encontrado o stock inválido",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Producto no encontrado",
                                                                    value = """
                                {
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "Producto no encontrado con el id: 999",
                                  "timestamp": "2025-12-18T10:30:00",
                                  "path": "/api/v1/products/999/stock"
                                }
                                """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Stock negativo",
                                                                    value = """
                                {
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "El stock no puede ser negativo",
                                  "timestamp": "2025-12-18T10:30:00",
                                  "path": "/api/v1/products/10/stock"
                                }
                                """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "La Ruta no fue encontrada/no existe.",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 404,
                              "error": "Not Found",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/products/10/stoc"
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "message": "Error al conectar con la base de datos",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/products/10/stock"
                            }
                            """
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/products/{id}/name",
                    method = RequestMethod.PUT,
                    beanClass = ProductHandler.class,
                    beanMethod = "updateName",
                    operation = @Operation(
                            operationId = "updateProductName",
                            tags = {"Productos"},
                            summary = "Actualizar nombre de producto",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del producto")},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Nuevo nombre del producto",
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = ProductRequest.class),
                                            examples = @ExampleObject(
                                                    value = """
                        {
                          "nombre_producto": "AirPods Pro 2 Edición 2025"
                        }
                        """
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Nombre actualizado exitosamente",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 200,
                              "message": "Nombre de producto actualizado exitosamente.",
                              "data": {
                                "id": 10,
                                "branchId": 4,
                                "name": "AirPods Pro 2 Edición 2025",
                                "stock": 180
                              }
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Producto no encontrado o nombre inválido",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Producto no encontrado",
                                                                    value = """
                                {
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "Producto no encontrado con el id: 999",
                                  "timestamp": "2025-12-18T10:30:00",
                                  "path": "/api/v1/products/999/name"
                                }
                                """
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Nombre vacío",
                                                                    value = """
                                {
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "El nombre no puede estar vacío",
                                  "timestamp": "2025-12-18T10:30:00",
                                  "path": "/api/v1/products/10/name"
                                }
                                """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "La Ruta no fue encontrada/no existe.",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 404,
                              "error": "Not Found",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/products/10/nam"
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "message": "Error al conectar con la base de datos",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/products/10/name"
                            }
                            """
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/products/{id}",
                    method = RequestMethod.DELETE,
                    beanClass = ProductHandler.class,
                    beanMethod = "delete",
                    operation = @Operation(
                            operationId = "deleteProduct",
                            tags = {"Productos"},
                            summary = "Eliminar producto",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del producto")},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Producto eliminado exitosamente",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 200,
                              "message": "Producto eliminado exitosamente."
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Producto no encontrado",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 400,
                              "error": "Bad Request",
                              "message": "Producto no encontrado con el id: 999",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/products/999"
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "La Ruta no fue encontrada/no existe.",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 404,
                              "error": "Not Found",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/products/10"
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "message": "Error al conectar con la base de datos",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/products/10"
                            }
                            """
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/franchises/{franchiseId}/top-stock-products",
                    method = RequestMethod.GET,
                    beanClass = ProductHandler.class,
                    beanMethod = "getTopStockByFranchise",
                    operation = @Operation(
                            operationId = "getTopStockByFranchise",
                            tags = {"Productos"},
                            summary = "Producto con mayor stock por sucursal",
                            description = "Retorna el producto con mayor stock de cada sucursal de una franquicia",
                            parameters = {@Parameter(in = ParameterIn.PATH, name = "franchiseId", description = "ID de la franquicia")},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Listado de productos con mayor stock por sucursal",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 200,
                              "message": "Productos con mayor stock por sucursal obtenidos exitosamente.",
                              "data": [
                                {
                                  "branch_id": 4,
                                  "branch_name": "Sucursal Bogotá Norte",
                                  "product_name": "AirPods Pro 2",
                                  "stock": 200
                                },
                                {
                                  "branch_id": 5,
                                  "branch_name": "Sucursal Centro",
                                  "product_name": "Sony WH-1000XM5",
                                  "stock": 160
                                },
                                {
                                  "branch_id": 6,
                                  "branch_name": "Sucursal Bogotá Centro",
                                  "product_name": "JBL Flip 6",
                                  "stock": 250
                                }
                              ]
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Franquicia no encontrada o error de validación",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 400,
                              "error": "Bad Request",
                              "message": "Franquicia no encontrada con el id: 999",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/franchises/999/top-stock-products"
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "La Ruta no fue encontrada/no existe.",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 404,
                              "error": "Not Found",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/franchises/1/top-stock-product"
                            }
                            """
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponse.class),
                                                    examples = @ExampleObject(
                                                            value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "message": "Error al conectar con la base de datos",
                              "timestamp": "2025-12-18T10:30:00",
                              "path": "/api/v1/franchises/1/top-stock-products"
                            }
                            """
                                                    )
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions
                .route(POST(API_V1 + "/products/create").and(accept(APPLICATION_JSON)),
                        handler::create)
                .andRoute(PUT(API_V1 + "/products/{id}/stock").and(accept(APPLICATION_JSON)),
                        handler::updateStock)
                .andRoute(PUT(API_V1 + "/products/{id}/name").and(accept(APPLICATION_JSON)),
                        handler::updateName)
                .andRoute(DELETE(API_V1 + "/products/{id}"),
                        handler::delete)
                .andRoute(GET(API_V1 + "/franchises/{franchiseId}/top-stock-products"),
                        handler::getTopStockByFranchise);
    }

}
