import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-material.reducer';

export const HkjMaterialDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjMaterialEntity = useAppSelector(state => state.hkjMaterial.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjMaterialDetailsHeading">
          <Translate contentKey="serverApp.hkjMaterial.detail.title">HkjMaterial</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="serverApp.hkjMaterial.name">Name</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialEntity.name}</dd>
          <dt>
            <span id="unit">
              <Translate contentKey="serverApp.hkjMaterial.unit">Unit</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialEntity.unit}</dd>
          <dt>
            <span id="pricePerUnit">
              <Translate contentKey="serverApp.hkjMaterial.pricePerUnit">Price Per Unit</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialEntity.pricePerUnit}</dd>
          <dt>
            <span id="coverImage">
              <Translate contentKey="serverApp.hkjMaterial.coverImage">Cover Image</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialEntity.coverImage}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.hkjMaterial.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjMaterial.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjMaterial.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjMaterialEntity.createdDate ? (
              <TextFormat value={hkjMaterialEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjMaterial.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjMaterialEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjMaterial.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjMaterialEntity.lastModifiedDate ? (
              <TextFormat value={hkjMaterialEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/hkj-material" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-material/${hkjMaterialEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjMaterialDetail;
