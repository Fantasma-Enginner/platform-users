import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';

import { ResourceDTO, RecursosService } from '@tsir/resources-api';
import { PerfilesService } from '@tsir/users-api';
import Swal from 'sweetalert2';

@Component({
  selector: 'users-authorization',
  templateUrl: './authorization.component.html',
  styleUrls: ['./authorization.component.css']
})
export class AuthorizationComponent implements OnInit, OnChanges {

  message?: string;
  error?: boolean = false;
  success?: boolean = false;

  sections: Domain[];
  resources: ResourceDTO[];
  mapDomains = new Map<number, Domain>();
  mapModules = new Map<number, Module>();
  mapOperations = new Map<number, Operation>();
  selectedDomain: Domain;
  toast = Swal.mixin({
    toast: true,
    position: 'bottom-right',
    showConfirmButton: false,
    timer: 1500,
    timerProgressBar: true,
    didOpen: (t) => {
      t.addEventListener('mouseenter', Swal.stopTimer);
      t.addEventListener('mouseleave', Swal.resumeTimer);
    }
  });


  authorizations: number[];

  @Input() profileId: number;

  constructor(
    private resourcesService: RecursosService,
    private profilesService: PerfilesService
  ) { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    for (let property in changes) {
      if (property === 'profileId') {
        this.checkOptions();
      }
    }
  }

  private checkOptions(): void {
    this.setSecurityConfiguration();
    this.resourcesService.findResources().subscribe(
      (data) => {
        this.resources = data;
        this.loadAuthorizations();
      },
      (error) => {
        this.setError('Ha occurrido un error consultando recursos');
      }
    );
  }

  private setSecurityConfiguration(): void {
    this.profilesService.configuration.accessToken = localStorage.getItem('token');
    this.resourcesService.configuration.accessToken = localStorage.getItem('token');
  }

  private loadAuthorizations(): void {
    this.profilesService.getProfileResources(this.profileId).subscribe(
      (data) => {
        this.authorizations = data;
        this.loadOptions();
      },
      (error) => {
        this.setError("Falla consultando permisos");
      }
    );
  }

  private loadOptions(): void {
    if (this.resources) {
      this.resources.forEach(r => {
        if (!r.parent && !this.mapDomains.has(r.id)) {
          this.mapDomains.set(r.id, new Domain(r.label, r.icon, []));
        } else {
          if (r.path) {
            let app = new Module(r.label, r.path, r.icon, []);
            let domain = this.mapDomains.get(r.parent);
            if (domain) {
              domain.modules.push(app);
            }
            this.mapModules.set(r.id, app);
          } else {
            let module = this.mapModules.get(r.parent);
            let assign = this.authorizations.includes(r.id);
            let operation = new Operation(r.id, r.code, r.label, assign);
            if (module) {
              module.operations.push(operation);
            }
            this.mapOperations.set(r.id, operation);
          }
        }
      });
      this.sections = [...this.mapDomains.values()];
    }
  }

  onChangeCheckBox(event: any, operation: Operation): void {
    let resourceId = event.target.value;
    if (event.target.checked) {
      this.grantResource(resourceId, operation.name);
    } else {
      this.revokeResource(resourceId, operation.name);
    }
  }

  private grantResource(resourceId: number, resourceName: string): void {
    let operation = this.mapOperations.get(resourceId);
    this.profilesService.grantResource([resourceId], this.profileId).subscribe(
      (data) => {
        this.authorizations.push(resourceId);
        this.setSuccess('Operación asignada correctamente: ' + resourceName);
        if (operation) {
          operation.assign = true;
        }
      },
      (error) => {
        this.setError('Ha fallado la asignación de operación: ' + resourceName);
        if (operation) {
          operation.assign = false;
        }
      }
    );
  }

  private revokeResource(resourceId: number, resourceName: string): void {
    let operation = this.mapOperations.get(resourceId);
    this.profilesService.revokeResource(this.profileId, resourceId).subscribe(
      (data) => {
        this.setSuccess('Operación revocada correctamente: ' + resourceName);
        let index = this.authorizations.indexOf(resourceId);
        if (index != 1) {
          this.authorizations.splice(index, 1);
        }
        if (operation) {
          operation.assign = false;
        }
      },
      (error) => {
        this.setError('Ha fallado la revocación de operación: ' + resourceName);
        if (operation) {
          operation.assign = true;
        }
      }
    );
  }

  private setSuccess(message: string) {
    this.success = true;
    this.error = false;
    this.message = message;
    this.toast.fire({
      icon: 'success',
      title: 'Authorizaciones',
      text: message
    });
  }

  private setError(message: string) {


    this.success = false;
    this.error = true;
    this.message = message;
    this.toast.fire({
      icon: 'error',
      title: 'Authorizaciones',
      text: message
    });
  }

  clearMessage(): void {
    this.error = false;
    this.success = false;
    this.message = null;
  }

  setSection(domain: Domain): void {
    this.selectedDomain = domain;
  }

}

export class Domain {

  constructor(public name: string, public icon: string, public modules: Module[]) {
  }

  isEmpty(): boolean {
    return this.modules ? this.modules.length === 0 : true;
  }

}

export class Module {

  constructor(public name: string, public url: string, public icon: string, public operations: Operation[]) {
  }

  isEmpty(): boolean {
    return this.operations ? this.operations.length === 0 : true;
  }

}

export class Operation {

  constructor(public id: number, public code: number, public name: string, public assign?: boolean) {
  }

}
